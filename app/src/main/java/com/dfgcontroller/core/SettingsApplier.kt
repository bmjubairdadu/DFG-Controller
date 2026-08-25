package com.dfgcontroller.core

import android.content.Context
import android.util.Log
import com.dfgcontroller.data.SettingsRepository
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.flow.first
import com.dfgcontroller.core.SysfsPaths

object SettingsApplier {

    suspend fun applyAll(context: Context) {
        val repository = SettingsRepository(context)
        
        // Root access is required
        if (!ShellManager.isRootAvailable()) {
            Log.w("SettingsApplier", "Root not available, skipping application")
            return
        }

        try {
            // Unified Profile - Safest approach
            val currentProfile = repository.currentProfile.first()
            if (currentProfile != "Balanced" || ShellManager.isLegacyKernelDetected()) {
                 // If legacy or custom needs, apply manual nodes with safety
                 applyManual(repository)
            } else {
                 // Unified API apply
                 Log.i("SettingsApplier", "Applying unified profile: $currentProfile")
                 ShellManager.writeSysfs(SysfsPaths.DFG_PROFILE, currentProfile.lowercase())
            }
        } catch (e: Exception) {
            Log.e("SettingsApplier", "Error during profile application", e)
        }
    }

    private suspend fun applyManual(repository: SettingsRepository) {
        // CPU Governor
        repository.cpuGovernor.first()?.takeIf { it.isNotBlank() && it != "-" }?.let { gov ->
            Log.d("SettingsApplier", "Applying governor: $gov")
            if (!ShellManager.writeSysfs(SysfsPaths.DFG_CPU_GOVERNOR, gov)) {
                val cpuCount = ShellManager.getCpuCount()
                for (i in 0 until cpuCount) {
                    ShellManager.writeSysfs(String.format(SysfsPaths.CPU_GOVERNOR_ALL, i), gov)
                }
            }
        }

        // I/O Scheduler
        repository.ioScheduler.first()?.takeIf { it.isNotBlank() && it != "-" }?.let { sched ->
            Log.d("SettingsApplier", "Applying scheduler: $sched")
            if (!ShellManager.writeSysfs(SysfsPaths.DFG_IO_SCHEDULER, sched)) {
                ShellManager.writeSysfs(SysfsPaths.IO_SCHEDULER, sched)
            }
        }

        // Display (KCAL)
        repository.kcalRgb.first()?.takeIf { it.isNotBlank() }?.let { rgb ->
            ShellManager.writeSysfs(SysfsPaths.KCAL_CTRL, rgb)
        }

        ShellManager.writeSysfs(SysfsPaths.KCAL_ENABLE, if (repository.kcalEnabled.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.GPU_CONSERVATIVE, if (repository.gpuConservative.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.FAST_CHARGE, if (repository.fastCharge.first()) "1" else "0")
        
        val dynFsync = if (repository.dynamicFsync.first()) "1" else "0"
        if (!ShellManager.writeSysfs(SysfsPaths.DFG_DYN_FSYNC, dynFsync)) {
            ShellManager.writeSysfs(SysfsPaths.DYNAMIC_FSYNC, dynFsync)
        }

        ShellManager.writeSysfs(SysfsPaths.CHARGE_PRIORITY, if (repository.chargePriority.first()) "1" else "0")
        
        // TCP Congestion
        repository.tcpCongestion.first()?.takeIf { it.isNotBlank() && it != "-" }?.let { algo ->
            if (!ShellManager.writeSysfs(SysfsPaths.DFG_TCP_CONGESTION, algo)) {
                ShellManager.writeSysfs(SysfsPaths.TCP_CONGESTION, algo)
            }
        }

        // ZRAM - REMOVED AUTOMATIC APPLICATION - This was likely causing the crash
        // User must enable ZRAM manually in the UI.

        // DPI
        repository.dpiValue.first()?.let { dpi ->
            if (dpi in 160..640) {
                Log.i("SettingsApplier", "Applying DPI: $dpi")
                Shell.cmd("wm density $dpi").exec()
            }
        }

        ShellManager.writeSysfs(SysfsPaths.TOUCH_BOOST_ENABLED, if (repository.touchBoostEnabled.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.TOUCH_BOOST_DURATION, repository.touchBoostDuration.first().toString())
        ShellManager.writeSysfs(SysfsPaths.LMK_AGGRESSIVE, if (repository.lmkAggressive.first()) "1" else "0")
    }
}
