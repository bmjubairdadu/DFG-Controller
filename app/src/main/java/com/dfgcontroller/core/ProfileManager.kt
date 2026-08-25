package com.dfgcontroller.core

import com.dfgcontroller.ui.models.KernelProfile
import android.util.Log

class ProfileManager(private val sysfsManager: SysfsManager) {

    suspend fun applyProfile(profile: KernelProfile): Boolean {
        // Try unified profile write first (Performance / Balanced / Battery)
        if (!profile.isCustom) {
            val profileValue = profile.name.lowercase()
            if (sysfsManager.write(SysfsPaths.DFG_PROFILE, profileValue)) {
                Log.d("ProfileManager", "Applied unified profile: $profileValue")
                return true
            }
        }

        // Fallback to manual tunables for older kernels or custom profiles
        var allSuccess = true

        profile.cpuMinFreq?.let {
            if (!sysfsManager.write(SysfsPaths.CPU_MIN_FREQ, it)) allSuccess = false
        }
        profile.cpuMaxFreq?.let {
            if (!sysfsManager.write(SysfsPaths.CPU_MAX_FREQ, it)) allSuccess = false
        }
        profile.governor?.let {
            if (!sysfsManager.write(SysfsPaths.DFG_CPU_GOVERNOR, it) && 
                !sysfsManager.write(SysfsPaths.CPU_GOVERNOR, it)) allSuccess = false
        }
        profile.ioScheduler?.let {
            if (!sysfsManager.write(SysfsPaths.DFG_IO_SCHEDULER, it) && 
                !sysfsManager.write(SysfsPaths.IO_SCHEDULER, it)) allSuccess = false
        }
        profile.boostMs?.let {
            if (!sysfsManager.write(SysfsPaths.BOOST_MS, it.toString())) allSuccess = false
        }
        
        Log.d("ProfileManager", "Applied manual/fallback profile: ${profile.name}, success: $allSuccess")
        return allSuccess
    }
}
