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
        try {
            Log.d("ShellManager", "Performing kernel capability check...")
            
            // Check for the unified profile node - the gold standard for v2.0
            val profileExists = Shell.cmd("[ -e \"${SysfsPaths.DFG_PROFILE}\" ]").exec().isSuccess
            if (profileExists) {
                Log.i("ShellManager", "Unified DFG API detected at ${SysfsPaths.DFG_PROFILE}")
                _isLegacyKernel = false
                return@withContext
            }
            
            // Check for base directory
            val baseExists = Shell.cmd("[ -d \"${SysfsPaths.DFG_BASE}\" ]").exec().isSuccess
            if (baseExists) {
                Log.w("ShellManager", "DFG directory found but 'profile' node is missing. Falling back to legacy mode.")
                val contents = Shell.cmd("ls \"${SysfsPaths.DFG_BASE}\"").exec().out.joinToString(", ")
                Log.d("ShellManager", "DFG directory contents: $contents")
            } else {
                Log.w("ShellManager", "DFG base directory NOT found. Kernel may not be DaisyForGaming.")
            }

            _isLegacyKernel = true
        } catch (e: Exception) {
            Log.e("ShellManager", "Kernel check failed", e)
            _isLegacyKernel = true
        }
    }

    suspend fun isLegacyKernelDetected(): Boolean {
        if (_isLegacyKernel == null) checkLegacyStatus()
        return _isLegacyKernel ?: true
    }

    suspend fun checkKernelConfig(key: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // First check /proc/config.gz (standard)
            val configGz = Shell.cmd("zcat /proc/config.gz | grep \"$key=y\"").exec()
            if (configGz.isSuccess) return@withContext true

            // Fallback: check if the kernel exposes it elsewhere (some kernels do)
            val ikconfig = Shell.cmd("grep \"$key=y\" /proc/ikconfig").exec()
            ikconfig.isSuccess
        } catch (e: Exception) {
            false
        }
    }

    suspend fun writeSysfs(path: String, value: String): Boolean = withContext(Dispatchers.IO) {
        try {
            if (value.isBlank() || path.isBlank()) return@withContext false
            
            // Use quotes and handle potential special characters
            val result = Shell.cmd("echo \"$value\" > \"$path\"").exec()
            if (!result.isSuccess) {
                val check = Shell.cmd("[ -e \"$path\" ]").exec()
                if (!check.isSuccess) {
                    Log.w("ShellManager", "Path does not exist: $path")
                } else {
                    Log.e("ShellManager", "Failed to write to $path (Code: ${result.code})")
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
            if (path.isBlank()) return@withContext null
            
            var result = Shell.cmd("cat \"$path\"").exec()
            if (result.isSuccess) {
                result.out.firstOrNull()?.trim()
            } else if (fallback != null && fallback.isNotBlank()) {
                Log.i("ShellManager", "Primary path failed, trying fallback: $fallback")
                result = Shell.cmd("cat \"$fallback\"").exec()
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
