package com.daisyforgaming.ui.models

import kotlinx.serialization.Serializable

@Serializable
data class KernelProfile(
    val name: String,
    val cpuMinFreq: String? = null,
    val cpuMaxFreq: String? = null,
    val governor: String? = null,
    val ioScheduler: String? = null,
    val boostMs: Int? = null,
    val isCustom: Boolean = false
) {
    companion object {
        val Battery = KernelProfile(
            name = "Battery",
            cpuMinFreq = "652800",
            cpuMaxFreq = "1036800",
            governor = "powersave",
            ioScheduler = "noop"
        )

        val Balanced = KernelProfile(
            name = "Balanced",
            cpuMinFreq = "652800",
            cpuMaxFreq = "1804800",
            governor = "schedutil",
            ioScheduler = "cfq"
        )

        val Performance = KernelProfile(
            name = "Performance",
            cpuMinFreq = "1036800",
            cpuMaxFreq = "2016000",
            governor = "performance",
            ioScheduler = "deadline"
        )
    }
}
