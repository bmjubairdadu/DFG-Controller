package com.daisyforgaming.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val CPU_GOVERNOR = stringPreferencesKey("cpu_governor")
        val IO_SCHEDULER = stringPreferencesKey("io_scheduler")
        val KCAL_RGB = stringPreferencesKey("kcal_rgb")
        val KCAL_ENABLED = booleanPreferencesKey("kcal_enabled")
        val GPU_CONSERVATIVE = booleanPreferencesKey("gpu_conservative")
        val FAST_CHARGE = booleanPreferencesKey("fast_charge")
        val DYNAMIC_FSYNC = booleanPreferencesKey("dynamic_fsync")
        val GAMING_MODE = booleanPreferencesKey("gaming_mode")
        val BYPASS_TRIGGER_ENABLED = booleanPreferencesKey("bypass_trigger_enabled")
        val BYPASS_TRIGGER_PACKAGE = stringPreferencesKey("bypass_trigger_package")
        val SHOW_SYSTEM_APPS = booleanPreferencesKey("show_system_apps")
        val GAME_APPS = stringSetPreferencesKey("game_apps")
        val KILL_WHITELIST = stringSetPreferencesKey("kill_whitelist")
        val GAME_MODE_ENABLED = booleanPreferencesKey("game_mode_enabled")
        val ZRAM_ENABLED = booleanPreferencesKey("zram_enabled")
        val ZRAM_SIZE = stringPreferencesKey("zram_size")
        val CHARGE_PRIORITY = booleanPreferencesKey("charge_priority")
        val TCP_CONGESTION = stringPreferencesKey("tcp_congestion")
        val DPI_VALUE = intPreferencesKey("dpi_value")
        val TOUCH_BOOST_ENABLED = booleanPreferencesKey("touch_boost_enabled")
        val TOUCH_BOOST_DURATION = intPreferencesKey("touch_boost_duration")
        val LMK_AGGRESSIVE = booleanPreferencesKey("lmk_aggressive")
        val CURRENT_PROFILE = stringPreferencesKey("current_profile")
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    val cpuGovernor: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.CPU_GOVERNOR] }
    val ioScheduler: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.IO_SCHEDULER] }
    val kcalRgb: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.KCAL_RGB] }
    val kcalEnabled: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.KCAL_ENABLED] ?: false }
    val gpuConservative: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.GPU_CONSERVATIVE] ?: false }
    val fastCharge: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.FAST_CHARGE] ?: false }
    val dynamicFsync: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.DYNAMIC_FSYNC] ?: false }
    val gamingMode: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.GAMING_MODE] ?: false }
    val bypassTriggerEnabled: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.BYPASS_TRIGGER_ENABLED] ?: false }
    val bypassTriggerPackage: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.BYPASS_TRIGGER_PACKAGE] }
    val showSystemApps: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.SHOW_SYSTEM_APPS] ?: false }
    val gameApps: Flow<Set<String>> = context.dataStore.data.map { it[PreferencesKeys.GAME_APPS] ?: emptySet() }
    val killWhitelist: Flow<Set<String>> = context.dataStore.data.map { it[PreferencesKeys.KILL_WHITELIST] ?: setOf("com.android.systemui", "com.google.android.gms", "com.daisyforgaming") }
    val gameModeEnabled: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.GAME_MODE_ENABLED] ?: false }
    val zramEnabled: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.ZRAM_ENABLED] ?: false }
    val zramSize: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.ZRAM_SIZE] ?: "512M" }
    val chargePriority: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.CHARGE_PRIORITY] ?: false }
    val tcpCongestion: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.TCP_CONGESTION] ?: "cubic" }
    val dpiValue: Flow<Int?> = context.dataStore.data.map { it[PreferencesKeys.DPI_VALUE] }
    val touchBoostEnabled: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.TOUCH_BOOST_ENABLED] ?: false }
    val touchBoostDuration: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.TOUCH_BOOST_DURATION] ?: 60 }
    val lmkAggressive: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.LMK_AGGRESSIVE] ?: false }
    val currentProfile: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.CURRENT_PROFILE] ?: "Balanced" }
    val themeColor: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.THEME_COLOR] ?: "Cyan" }

    suspend fun saveCpuGovernor(value: String) {
        context.dataStore.edit { it[PreferencesKeys.CPU_GOVERNOR] = value }
    }

    suspend fun saveIoScheduler(value: String) {
        context.dataStore.edit { it[PreferencesKeys.IO_SCHEDULER] = value }
    }

    suspend fun saveKcalRgb(value: String) {
        context.dataStore.edit { it[PreferencesKeys.KCAL_RGB] = value }
    }

    suspend fun saveKcalEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.KCAL_ENABLED] = value }
    }

    suspend fun saveGpuConservative(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.GPU_CONSERVATIVE] = value }
    }

    suspend fun saveFastCharge(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.FAST_CHARGE] = value }
    }

    suspend fun saveDynamicFsync(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.DYNAMIC_FSYNC] = value }
    }

    suspend fun saveGamingMode(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.GAMING_MODE] = value }
    }

    suspend fun saveBypassTriggerEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.BYPASS_TRIGGER_ENABLED] = value }
    }

    suspend fun saveBypassTriggerPackage(value: String?) {
        context.dataStore.edit { 
            if (value == null) it.remove(PreferencesKeys.BYPASS_TRIGGER_PACKAGE)
            else it[PreferencesKeys.BYPASS_TRIGGER_PACKAGE] = value 
        }
    }

    suspend fun saveShowSystemApps(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.SHOW_SYSTEM_APPS] = value }
    }

    suspend fun saveGameApps(apps: Set<String>) {
        context.dataStore.edit { it[PreferencesKeys.GAME_APPS] = apps }
    }

    suspend fun saveKillWhitelist(apps: Set<String>) {
        context.dataStore.edit { it[PreferencesKeys.KILL_WHITELIST] = apps }
    }

    suspend fun saveGameModeEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.GAME_MODE_ENABLED] = value }
    }

    suspend fun saveZramEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.ZRAM_ENABLED] = value }
    }

    suspend fun saveZramSize(value: String) {
        context.dataStore.edit { it[PreferencesKeys.ZRAM_SIZE] = value }
    }

    suspend fun saveChargePriority(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.CHARGE_PRIORITY] = value }
    }

    suspend fun saveTcpCongestion(value: String) {
        context.dataStore.edit { it[PreferencesKeys.TCP_CONGESTION] = value }
    }

    suspend fun saveDpiValue(value: Int) {
        context.dataStore.edit { it[PreferencesKeys.DPI_VALUE] = value }
    }

    suspend fun clearDpiValue() {
        context.dataStore.edit { it.remove(PreferencesKeys.DPI_VALUE) }
    }

    suspend fun saveTouchBoostEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.TOUCH_BOOST_ENABLED] = value }
    }

    suspend fun saveTouchBoostDuration(value: Int) {
        context.dataStore.edit { it[PreferencesKeys.TOUCH_BOOST_DURATION] = value }
    }

    suspend fun saveLmkAggressive(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.LMK_AGGRESSIVE] = value }
    }

    suspend fun saveCurrentProfile(value: String) {
        context.dataStore.edit { it[PreferencesKeys.CURRENT_PROFILE] = value }
    }

    suspend fun saveThemeColor(value: String) {
        context.dataStore.edit { it[PreferencesKeys.THEME_COLOR] = value }
    }
}
