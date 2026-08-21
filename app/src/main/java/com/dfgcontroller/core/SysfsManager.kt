package com.dfgcontroller.core

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface SysfsManager {
    suspend fun read(path: String): String?
    suspend fun write(path: String, value: String): Boolean
}

class RootSysfsManager : SysfsManager {
    override suspend fun read(path: String): String? = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("cat $path").exec()
            if (result.isSuccess) result.out.firstOrNull() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun write(path: String, value: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Shell.cmd("echo \"$value\" > $path").exec()
            result.isSuccess
        } catch (e: Exception) {
            false
        }
    }
}

class NoRootSysfsManager : SysfsManager {
    override suspend fun read(path: String): String? = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (file.exists() && file.canRead()) {
                file.readText().trim()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun write(path: String, value: String): Boolean {
        // Non-root cannot write to most sysfs nodes
        return false
    }
}
