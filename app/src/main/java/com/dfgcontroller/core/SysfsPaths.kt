package com.dfgcontroller.core

object SysfsPaths {
    // Standard Linux/Android paths
    const val PROC_VERSION = "/proc/version"
    const val CPU_BASE = "/sys/devices/system/cpu/"
    const val CPU_AVAILABLE_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies"
    
    // DFG Specific (DaisyForGaming Kernel)
    const val DFG_BASE = "/sys/devices/platform/dfg/"
    const val CPU_MIN_FREQ = "${DFG_BASE}cpu_min_freq"
    const val CPU_MAX_FREQ = "${DFG_BASE}cpu_max_freq"
    const val CPU_GOVERNOR = "${DFG_BASE}governor"
    const val THERMAL_STATUS = "${DFG_BASE}thermal_status"
    const val BOOST_MS = "${DFG_BASE}boost_ms"

    // Standard Fallbacks
    const val FALLBACK_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"
    const val FALLBACK_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq"
    const val FALLBACK_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq"
    const val CPU_GOVERNOR_ALL = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor"
    
    // Graphics & Display
    const val KCAL_CTRL = "/sys/devices/platform/kcal_ctrl.0/kcal"
    const val KCAL_ENABLE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable"
    const val GPU_CONSERVATIVE = "/sys/class/kgsl/kgsl-3d0/devfreq/adreno_gpu_pwr_stats"
    
    // Battery & Charging
    const val FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge"
    const val AUTO_FAST_CHARGE_STATUS = "/sys/class/power_supply/battery/fast_charge_status"
    const val BYPASS_CHARGING = "/sys/class/power_supply/battery/input_suspend"
    const val CHARGE_PRIORITY = "/sys/class/power_supply/battery/charge_priority"
    const val DYNAMIC_FSYNC = "/sys/kernel/dyn_fsync/Dynamic_fsync"

    // Memory
    const val ZRAM_RESET = "/sys/block/zram0/reset"
    const val ZRAM_DISKSIZE = "/sys/block/zram0/disksize"
    const val ZRAM_COMPACT = "/sys/block/zram0/compact"
    const val MEM_INFO = "/proc/meminfo"
    
    // Misc
    const val WAKELOCKS = "/proc/wakelocks"
    const val TCP_CONGESTION = "/proc/sys/net/ipv4/tcp_congestion_control"
    const val GAME_MODE = "/sys/devices/platform/dfg/game_mode"
    const val IO_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler"
    
    // Smoothness
    const val TOUCH_BOOST_ENABLED = "/sys/module/input_boost/parameters/enabled"
    const val TOUCH_BOOST_DURATION = "/sys/module/input_boost/parameters/boost_duration_ms"
    const val LMK_AGGRESSIVE = "/sys/module/lowmemorykiller/parameters/lmk_aggressive"
}
