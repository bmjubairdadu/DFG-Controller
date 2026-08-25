package com.dfgcontroller.core

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

object ShellManager {

    private var _isLegacyKernel: Boolean? = null

    suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            Shell.getShell().isRoot
        } catch (e: Exception) {
            Log.e("ShellManager", "Error checking root", e)
            false
        }
    }

    suspend fun checkLegacyStatus() = withContext(Dispatchers.IO) {
        _isLegacyKernel = !Shell.cmd("test -e ${SysfsPaths.DFG_BASE}profile").exec().isSuccess
    }

    suspend fun isLegacyKernelDetected(): Boolean {
        if (_isLegacyKernel == null) isRootAvailable()
        return _isLegacyKernel ?: true
    }

    suspend fun writeSysfs(path: String, value: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("echo $value > $path").exec()
            if (!result.isSuccess) {
                val check = Shell.cmd("test -e $path").exec()
                if (!check.isSuccess) {
                    Log.w("ShellManager", "Path does not exist: $path")
                } else {
                    Log.e("ShellManager", "Failed to write to $path: ${result.err.joinToString("\n")}")
                }
            }
            result.isSuccess
        } catch (e: Exception) {
            Log.e("ShellManager", "Exception writing to $path", e)
            false
        }
    }

    suspend fun readSysfs(path: String, fallback: String? = null): String? = withContext(Dispatchers.IO) {
        try {
            var result = Shell.cmd("cat $path").exec()
            if (result.isSuccess) {
                result.out.firstOrNull()?.trim()
            } else if (fallback != null) {
                Log.i("ShellManager", "Primary path failed, trying fallback: $fallback")
                result = Shell.cmd("cat $fallback").exec()
                if (result.isSuccess) result.out.firstOrNull()?.trim() else null
            } else {
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
