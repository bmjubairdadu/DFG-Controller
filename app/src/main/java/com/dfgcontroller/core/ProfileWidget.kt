package com.dfgcontroller.core

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dfgcontroller.R

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.dfgcontroller.data.SettingsRepository

class ProfileWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.dfgcontroller.ACTION_APPLY_PROFILE") {
            val profileName = intent.getStringExtra("profile_name") ?: return
            
            // Use WorkManager to apply profile in background
            val workRequest = OneTimeWorkRequestBuilder<ProfileWorker>()
                .setInputData(Data.Builder().putString("profile_name", profileName).build())
                .build()
            
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(workRequest)
            
            // Listen for completion to update UI
            workManager.getWorkInfoByIdLiveData(workRequest.id).observeForever { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, ProfileWidget::class.java)
                    onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName))
                }
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_profile)

        val currentProfile = try {
            runBlocking { SettingsRepository(context).currentProfile.first() }
        } catch (_: Exception) { "Balanced" }

        val activeColor = 0xFF00FBFF.toInt() // Cyan
        val inactiveColor = 0xFFFFFFFF.toInt() // White

        views.setTextColor(R.id.btn_battery, if (currentProfile == "Battery") activeColor else inactiveColor)
        views.setTextColor(R.id.btn_balanced, if (currentProfile == "Balanced") activeColor else inactiveColor)
        views.setTextColor(R.id.btn_performance, if (currentProfile == "Performance") activeColor else inactiveColor)

        views.setOnClickPendingIntent(R.id.btn_battery, getProfilePendingIntent(context, "Battery"))
        views.setOnClickPendingIntent(R.id.btn_balanced, getProfilePendingIntent(context, "Balanced"))
        views.setOnClickPendingIntent(R.id.btn_performance, getProfilePendingIntent(context, "Performance"))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getProfilePendingIntent(context: Context, profileName: String): PendingIntent {
        val intent = Intent(context, ProfileWidget::class.java).apply {
            action = "com.dfgcontroller.ACTION_APPLY_PROFILE"
            putExtra("profile_name", profileName)
        }
        return PendingIntent.getBroadcast(
            context, 
            profileName.hashCode(), 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
