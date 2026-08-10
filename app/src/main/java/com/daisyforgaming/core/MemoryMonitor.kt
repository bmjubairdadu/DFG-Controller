package com.daisyforgaming.core

import java.io.File

data class MemoryStats(
    val totalRam: Long,
    val freeRam: Long,
    val totalZram: Long,
    val freeZram: Long
)

object MemoryMonitor {
    fun getStats(): MemoryStats {
        var totalRam = 0L
        var freeRam = 0L
        var totalZram = 0L
        var freeZram = 0L

        try {
            val lines = File(SysfsPaths.MEM_INFO).readLines()
            for (line in lines) {
                val parts = line.split(Regex("\\s+"))
                if (parts.size < 2) continue
                val value = parts[1].toLongOrNull() ?: 0L
                when {
                    line.startsWith("MemTotal:") -> totalRam = value
                    line.startsWith("MemFree:") || line.startsWith("Cached:") || line.startsWith("Buffers:") -> freeRam += value
                    line.startsWith("SwapTotal:") -> totalZram = value
                    line.startsWith("SwapFree:") -> freeZram = value
                }
            }
        } catch (e: Exception) {
            // Handle or log
        }

        return MemoryStats(totalRam, freeRam, totalZram, freeZram)
    }
}
