package com.dfgcontroller.core

import android.app.*
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dfgcontroller.MainActivity
import com.dfgcontroller.data.SettingsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class GameModeService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var monitoringJob: Job? = null
    private lateinit var repository: SettingsRepository
    private var isGameModeActive = false

    companion object {
        const val CHANNEL_ID = "game_mode_service"
        const val NOTIFICATION_ID = 1002
    }

    override fun onCreate() {
        super.onCreate()
        repository = SettingsRepository(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        startMonitoring()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Game Mode Monitor",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("DFG Game Mode Active")
            .setContentText("Monitoring for game launches.")
            .setSmallIcon(android.R.drawable.ic_menu_manage)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = serviceScope.launch {
            while (isActive) {
                val gameApps = repository.gameApps.first()
                val whitelist = repository.killWhitelist.first()
                val currentApp = getForegroundApp()

                if (gameApps.contains(currentApp)) {
                    if (!isGameModeActive) {
                        enableGameMode(whitelist)
                    }
                } else {
                    if (isGameModeActive) {
                        disableGameMode()
                    }
                }

                delay(3000)
            }
        }
    }

    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60, time)
        return stats?.maxByOrNull { it.lastTimeUsed }?.packageName
    }

    private suspend fun enableGameMode(whitelist: Set<String>) {
        ShellManager.writeSysfs(SysfsPaths.GAME_MODE, "1")
        isGameModeActive = true
        
        // Kill background apps not in whitelist
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses
        runningApps?.forEach { process ->
            if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && 
                !whitelist.contains(process.processName)) {
                am.killBackgroundProcesses(process.processName)
            }
        }
    }

    private suspend fun disableGameMode() {
        ShellManager.writeSysfs(SysfsPaths.GAME_MODE, "0")
        isGameModeActive = false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        runBlocking {
            disableGameMode()
        }
    }
}
