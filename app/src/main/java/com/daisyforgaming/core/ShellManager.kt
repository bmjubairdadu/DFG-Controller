package com.daisyforgaming.core

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

object ShellManager {

    suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
        Shell.getShell().isRoot
    }

    suspend fun writeSysfs(path: String, value: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("echo \"$value\" > $path").exec()
            result.isSuccess
        } catch (e: Exception) {
            false
        }
    }

    suspend fun readSysfs(path: String): String? = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("cat $path").exec()
            if (result.isSuccess) result.out.firstOrNull() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getCpuCount(): Int {
        return Runtime.getRuntime().availableProcessors()
    }
}
