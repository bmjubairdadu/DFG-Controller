package com.daisyforgaming.core

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.daisyforgaming.data.SettingsRepository
import com.daisyforgaming.ui.models.KernelProfile
import kotlinx.coroutines.flow.first

class ProfileWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): ListenableWorker.Result {
        val repository = SettingsRepository(applicationContext)
        val profileName = inputData.getString("profile_name") ?: repository.currentProfile.first()
        
        val profile = when (profileName) {
            "Performance" -> KernelProfile.Performance
            "Balanced" -> KernelProfile.Balanced
            "Battery" -> KernelProfile.Battery
            else -> return ListenableWorker.Result.failure()
        }

        val rootManager = RootSysfsManager()
        val profileManager = ProfileManager(rootManager)
        
        return if (profileManager.applyProfile(profile)) {
            ListenableWorker.Result.success()
        } else {
            ListenableWorker.Result.retry()
        }
    }
}
