package com.daisyforgaming.core

import android.content.Context
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object LogManager {

    suspend fun getKernelLogs(): List<String> = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("dmesg | tail -n 100").exec()
            if (result.isSuccess) result.out else listOf("Error reading dmesg (Root required)")
        } catch (e: Exception) {
            listOf("Exception: ${e.message}")
        }
    }

    suspend fun exportLogs(context: Context): String? = withContext(Dispatchers.IO) {
        try {
            val dmesgResult = Shell.cmd("dmesg").exec()
            val dmesg = if (dmesgResult.isSuccess) dmesgResult.out.joinToString("\n") else "Could not read dmesg"
            
            val logcatResult = Shell.cmd("logcat -d").exec()
            val logcat = if (logcatResult.isSuccess) logcatResult.out.joinToString("\n") else "Could not read logcat"
            
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "DFG_Log_$timestamp.txt"
            
            val directory = context.getExternalFilesDir(null) ?: return@withContext null
            val file = File(directory, fileName)
            
            file.writeText("--- KERNEL LOGS ---\n$dmesg\n\n--- APP LOGS ---\n$logcat")
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}
