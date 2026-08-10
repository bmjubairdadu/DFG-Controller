package com.daisyforgaming.core

import android.app.*
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.daisyforgaming.MainActivity
import com.daisyforgaming.R
import com.daisyforgaming.data.SettingsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class BypassChargingService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var monitoringJob: Job? = null
    private lateinit var repository: SettingsRepository
    private var isBypassActive = false

    companion object {
        const val CHANNEL_ID = "bypass_charging_service"
        const val NOTIFICATION_ID = 1001
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
            "Bypass Charging Monitor",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("DFG Bypass Charging Active")
            .setContentText("Monitoring foreground app for bypass charging.")
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = serviceScope.launch {
            while (isActive) {
                val targetPackage = repository.bypassTriggerPackage.first()
                val isTriggerEnabled = repository.bypassTriggerEnabled.first()

                if (isTriggerEnabled && targetPackage != null) {
                    val currentApp = getForegroundApp()
                    val isCharging = isDeviceCharging()

                    if (currentApp == targetPackage && isCharging) {
                        if (!isBypassActive) {
                            enableBypass()
                        }
                    } else {
                        if (isBypassActive) {
                            disableBypass()
                        }
                    }
                } else if (isBypassActive) {
                    disableBypass()
                }

                delay(2000) // Check every 2 seconds
            }
        }
    }

    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60, time)
        return stats?.maxByOrNull { it.lastTimeUsed }?.packageName
    }

    private fun isDeviceCharging(): Boolean {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, filter)
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    private suspend fun enableBypass() {
        if (ShellManager.writeSysfs(SysfsPaths.BYPASS_CHARGING, "1")) {
            // Verify write
            val current = ShellManager.readSysfs(SysfsPaths.BYPASS_CHARGING)
            if (current == "1") {
                isBypassActive = true
            } else {
                // Log error or notify user
            }
        }
    }

    private suspend fun disableBypass() {
        if (ShellManager.writeSysfs(SysfsPaths.BYPASS_CHARGING, "0")) {
            isBypassActive = false
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        runBlocking {
            disableBypass()
        }
    }
}
