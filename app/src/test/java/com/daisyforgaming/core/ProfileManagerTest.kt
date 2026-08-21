package com.daisyforgaming.core

import com.daisyforgaming.ui.models.KernelProfile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertEquals

class ProfileManagerTest {

    class MockSysfsManager : SysfsManager {
        val writes = mutableMapOf<String, String>()
        override suspend fun read(path: String): String? = null
        override suspend fun write(path: String, value: String): Boolean {
            writes[path] = value
            return true
        }
    }

    @Test
    fun testApplyProfile() = runBlocking {
        val mock = MockSysfsManager()
        val manager = ProfileManager(mock)
        val profile = KernelProfile.Performance

        val result = manager.applyProfile(profile)

        assertTrue(result)
        assertEquals(profile.cpuMinFreq, mock.writes[SysfsPaths.CPU_MIN_FREQ])
        assertEquals(profile.cpuMaxFreq, mock.writes[SysfsPaths.CPU_MAX_FREQ])
        assertEquals(profile.governor, mock.writes[SysfsPaths.CPU_GOVERNOR])
    }
}
