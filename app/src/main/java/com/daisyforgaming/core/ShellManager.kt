package com.daisyforgaming.core

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

object ShellManager {

    suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            Shell.getShell().isRoot
        } catch (e: Exception) {
            Log.e("ShellManager", "Error checking root", e)
            false
        }
    }

    suspend fun writeSysfs(path: String, value: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // Check if file exists first
            if (!Shell.cmd("test -e $path").exec().isSuccess) {
                Log.w("ShellManager", "Path does not exist: $path")
                return@withContext false
            }
            
            val result = Shell.cmd("echo $value > $path").exec()
            if (!result.isSuccess) {
                Log.e("ShellManager", "Failed to write to $path: ${result.err.joinToString("\n")}")
            }
            result.isSuccess
        } catch (e: Exception) {
            Log.e("ShellManager", "Exception writing to $path", e)
            false
        }
    }

    suspend fun readSysfs(path: String): String? = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("cat $path").exec()
            if (result.isSuccess) {
                result.out.firstOrNull()?.trim()
            } else {
                Log.e("ShellManager", "Failed to read $path: ${result.err.joinToString("\n")}")
                null
            }
        } catch (e: Exception) {
            Log.e("ShellManager", "Exception reading $path", e)
            null
        }
    }

    fun getCpuCount(): Int {
        return Runtime.getRuntime().availableProcessors()
    }
}
