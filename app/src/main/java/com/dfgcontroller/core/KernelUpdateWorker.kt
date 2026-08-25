package com.dfgcontroller.core

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class KernelUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val variant = KernelUpdateManager.checkForUpdate()
        return if (variant != null) {
            // In a production app, we would trigger a notification here.
            // For now, we return success to indicate the check finished.
            Result.success()
        } else {
            Result.success()
        }
    }

    companion object {
        private const val WORK_NAME = "KernelUpdateCheck"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = PeriodicWorkRequestBuilder<KernelUpdateWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
