package com.daisyforgaming.core

import android.content.Context
import com.daisyforgaming.data.SettingsRepository
import kotlinx.coroutines.flow.first
import com.daisyforgaming.core.SysfsPaths

object SettingsApplier {

    suspend fun applyAll(context: Context) {
        val repository = SettingsRepository(context)
        
        // Root access is required, libsu handles shell initiation
        if (!ShellManager.isRootAvailable()) return

        repository.cpuGovernor.first()?.let { gov ->
            val cpuCount = ShellManager.getCpuCount()
            for (i in 0 until cpuCount) {
                ShellManager.writeSysfs(String.format(SysfsPaths.CPU_GOVERNOR_ALL, i), gov)
            }
        }

        repository.ioScheduler.first()?.let { sched ->
            ShellManager.writeSysfs(SysfsPaths.IO_SCHEDULER, sched)
        }

        repository.kcalRgb.first()?.let { rgb ->
            ShellManager.writeSysfs(SysfsPaths.KCAL_CTRL, rgb)
        }

        ShellManager.writeSysfs(SysfsPaths.KCAL_ENABLE, if (repository.kcalEnabled.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.GPU_CONSERVATIVE, if (repository.gpuConservative.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.FAST_CHARGE, if (repository.fastCharge.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.DYNAMIC_FSYNC, if (repository.dynamicFsync.first()) "1" else "0")
        ShellManager.writeSysfs(SysfsPaths.CHARGE_PRIORITY, if (repository.chargePriority.first()) "1" else "0")
        
        repository.tcpCongestion.first()?.let { algo ->
            ShellManager.writeSysfs(SysfsPaths.TCP_CONGESTION, algo)
        }

        if (repository.zramEnabled.first()) {
            val size = repository.zramSize.first()
            ShellManager.writeSysfs(SysfsPaths.ZRAM_RESET, "1")
            ShellManager.writeSysfs(SysfsPaths.ZRAM_DISKSIZE, size)
            com.topjohnwu.superuser.Shell.cmd("mkswap /dev/block/zram0", "swapon /dev/block/zram0").exec()
        }
    }
}
