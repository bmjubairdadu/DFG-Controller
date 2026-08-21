package com.daisyforgaming.core

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
import com.daisyforgaming.R

class ProfileWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val profileName = intent.getStringExtra("profile_name") ?: return
        
        // Use WorkManager to apply profile in background
        val workRequest = OneTimeWorkRequestBuilder<ProfileWorker>()
            .setInputData(Data.Builder().putString("profile_name", profileName).build())
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_profile)

        views.setOnClickPendingIntent(R.id.btn_battery, getProfilePendingIntent(context, "Battery"))
        views.setOnClickPendingIntent(R.id.btn_balanced, getProfilePendingIntent(context, "Balanced"))
        views.setOnClickPendingIntent(R.id.btn_performance, getProfilePendingIntent(context, "Performance"))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getProfilePendingIntent(context: Context, profileName: String): PendingIntent {
        val intent = Intent(context, ProfileWidget::class.java).apply {
            action = "com.daisyforgaming.ACTION_APPLY_PROFILE"
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
