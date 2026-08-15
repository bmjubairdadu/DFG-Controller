package com.daisyforgaming.ui

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.daisyforgaming.ui.models.AppInfo
import androidx.lifecycle.AndroidViewModel
import com.daisyforgaming.core.SecurityUtils
import com.daisyforgaming.core.UpdateManager
import com.daisyforgaming.core.UpdateManifest
import androidx.lifecycle.viewModelScope
import android.app.ActivityManager
import android.content.Context
import com.daisyforgaming.core.MemoryMonitor
import com.daisyforgaming.core.MemoryStats
import com.topjohnwu.superuser.Shell
import com.daisyforgaming.core.ShellManager
import com.daisyforgaming.core.SettingsApplier
import com.daisyforgaming.core.SysfsPaths
import com.daisyforgaming.data.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    private val _isRootAvailable = MutableStateFlow<Boolean?>(null)
    val isRootAvailable: StateFlow<Boolean?> = _isRootAvailable.asStateFlow()

    private val _kernelVersion = MutableStateFlow("Loading...")
    val kernelVersion: StateFlow<String> = _kernelVersion.asStateFlow()

    private val _currentGovernor = MutableStateFlow("-")
    val currentGovernor: StateFlow<String> = _currentGovernor.asStateFlow()

    private val _availableGovernors = MutableStateFlow<List<String>>(emptyList())
    val availableGovernors: StateFlow<List<String>> = _availableGovernors.asStateFlow()

    private val _currentScheduler = MutableStateFlow("-")
    val currentScheduler: StateFlow<String> = _currentScheduler.asStateFlow()

    private val _gamingMode = MutableStateFlow(false)
    val gamingMode: StateFlow<Boolean> = _gamingMode.asStateFlow()

    private val _kcalR = MutableStateFlow(256f)
    val kcalR: StateFlow<Float> = _kcalR.asStateFlow()

    private val _kcalG = MutableStateFlow(256f)
    val kcalG: StateFlow<Float> = _kcalG.asStateFlow()

    private val _kcalB = MutableStateFlow(256f)
    val kcalB: StateFlow<Float> = _kcalB.asStateFlow()

    private val _kcalEnabled = MutableStateFlow(false)
    val kcalEnabled: StateFlow<Boolean> = _kcalEnabled.asStateFlow()

    private val _gpuConservative = MutableStateFlow(false)
    val gpuConservative: StateFlow<Boolean> = _gpuConservative.asStateFlow()

    private val _fastCharge = MutableStateFlow(false)
    val fastCharge: StateFlow<Boolean> = _fastCharge.asStateFlow()

    private val _dynamicFsync = MutableStateFlow(false)
    val dynamicFsync: StateFlow<Boolean> = _dynamicFsync.asStateFlow()

    private val _isApplyingProfile = MutableStateFlow(false)
    val isApplyingProfile: StateFlow<Boolean> = _isApplyingProfile.asStateFlow()

    private val _bypassTriggerEnabled = MutableStateFlow(false)
    val bypassTriggerEnabled: StateFlow<Boolean> = _bypassTriggerEnabled.asStateFlow()

    private val _bypassTriggerPackage = MutableStateFlow<String?>(null)
    val bypassTriggerPackage: StateFlow<String?> = _bypassTriggerPackage.asStateFlow()

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    private val _showSystemApps = MutableStateFlow(false)
    val showSystemApps: StateFlow<Boolean> = _showSystemApps.asStateFlow()

    private val _isAppListLoading = MutableStateFlow(false)
    val isAppListLoading: StateFlow<Boolean> = _isAppListLoading.asStateFlow()

    private val _gameApps = MutableStateFlow<Set<String>>(emptySet())
    val gameApps: StateFlow<Set<String>> = _gameApps.asStateFlow()

    private val _killWhitelist = MutableStateFlow<Set<String>>(emptySet())
    val killWhitelist: StateFlow<Set<String>> = _killWhitelist.asStateFlow()

    private val _memoryStats = MutableStateFlow(MemoryStats(0, 0, 0, 0))
    val memoryStats: StateFlow<MemoryStats> = _memoryStats.asStateFlow()

    private val _zramEnabled = MutableStateFlow(false)
    val zramEnabled: StateFlow<Boolean> = _zramEnabled.asStateFlow()

    private val _zramSize = MutableStateFlow("512M")
    val zramSize: StateFlow<String> = _zramSize.asStateFlow()

    private val _chargePriority = MutableStateFlow(false)
    val chargePriority: StateFlow<Boolean> = _chargePriority.asStateFlow()

    private val _autoFastChargeActive = MutableStateFlow(false)
    val autoFastChargeActive: StateFlow<Boolean> = _autoFastChargeActive.asStateFlow()

    private val _tcpCongestion = MutableStateFlow("-")
    val tcpCongestion: StateFlow<String> = _tcpCongestion.asStateFlow()

    private val _availableTcpCongestions = MutableStateFlow<List<String>>(emptyList())
    val availableTcpCongestions: StateFlow<List<String>> = _availableTcpCongestions.asStateFlow()

    private val _dpiInfo = MutableStateFlow("Loading...")
    val dpiInfo: StateFlow<String> = _dpiInfo.asStateFlow()

    private val _touchBoostEnabled = MutableStateFlow(false)
    val touchBoostEnabled: StateFlow<Boolean> = _touchBoostEnabled.asStateFlow()

    private val _touchBoostDuration = MutableStateFlow(60)
    val touchBoostDuration: StateFlow<Int> = _touchBoostDuration.asStateFlow()

    private val _lmkAggressive = MutableStateFlow(false)
    val lmkAggressive: StateFlow<Boolean> = _lmkAggressive.asStateFlow()

    private val _integrityStatus = MutableStateFlow<IntegrityStatus>(IntegrityStatus.CHECKING)
    val integrityStatus: StateFlow<IntegrityStatus> = _integrityStatus.asStateFlow()

    enum class IntegrityStatus {
        CHECKING, VALID, INVALID_SIGNATURE, DEBUGGER_CONNECTED
    }

    private val _updateManifest = MutableStateFlow<UpdateManifest?>(null)
    val updateManifest: StateFlow<UpdateManifest?> = _updateManifest.asStateFlow()

    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus.asStateFlow()

    init {
        checkIntegrity()
        checkRoot()
        loadInitialData()
        applySavedProfile()
        observeBypassSettings()
        observeNewSettings()
        startMemoryPolling()
        checkForUpdates()
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            _updateManifest.value = UpdateManager.checkForUpdate()
        }
    }

    fun startUpdate(context: Context) {
        _updateManifest.value?.let { manifest ->
            UpdateManager.downloadAndInstall(context, manifest) { status ->
                _updateStatus.value = status
            }
        }
    }

    fun dismissUpdate() {
        if (_updateManifest.value?.mandatory != true) {
            _updateManifest.value = null
        }
    }

    private fun checkIntegrity() {
        viewModelScope.launch {
            _integrityStatus.value = IntegrityStatus.VALID
        }
    }

    private fun observeBypassSettings() {
        viewModelScope.launch {
            repository.bypassTriggerEnabled.collect { _bypassTriggerEnabled.value = it }
        }
        viewModelScope.launch {
            repository.bypassTriggerPackage.collect { _bypassTriggerPackage.value = it }
        }
        viewModelScope.launch {
            repository.showSystemApps.collect { 
                _showSystemApps.value = it
                refreshAppList()
            }
        }
    }

    private fun observeNewSettings() {
        viewModelScope.launch {
            repository.gameApps.collect { _gameApps.value = it }
        }
        viewModelScope.launch {
            repository.killWhitelist.collect { _killWhitelist.value = it }
        }
        viewModelScope.launch {
            repository.zramEnabled.collect { _zramEnabled.value = it }
        }
        viewModelScope.launch {
            repository.zramSize.collect { _zramSize.value = it }
        }
        viewModelScope.launch {
            repository.chargePriority.collect { _chargePriority.value = it }
        }
        viewModelScope.launch {
            repository.tcpCongestion.collect { _tcpCongestion.value = it }
        }
        viewModelScope.launch {
            repository.touchBoostEnabled.collect { _touchBoostEnabled.value = it }
        }
        viewModelScope.launch {
            repository.touchBoostDuration.collect { _touchBoostDuration.value = it }
        }
        viewModelScope.launch {
            repository.lmkAggressive.collect { _lmkAggressive.value = it }
        }
    }

    private fun startMemoryPolling() {
        viewModelScope.launch {
            while (true) {
                _memoryStats.value = MemoryMonitor.getStats()
                _autoFastChargeActive.value = (ShellManager.readSysfs(SysfsPaths.AUTO_FAST_CHARGE_STATUS) ?: "0") == "1"
                delay(3000)
            }
        }
    }

    fun setBypassTriggerEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveBypassTriggerEnabled(enabled)
        }
    }

    fun setBypassTriggerPackage(packageName: String?) {
        viewModelScope.launch {
            repository.saveBypassTriggerPackage(packageName)
        }
    }

    fun setShowSystemApps(show: Boolean) {
        viewModelScope.launch {
            repository.saveShowSystemApps(show)
        }
    }

    fun refreshAppList() {
        viewModelScope.launch {
            _isAppListLoading.value = true
            val pm = getApplication<Application>().packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val filteredApps = apps.filter { 
                _showSystemApps.value || (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 
            }.map { app ->
                AppInfo(
                    name = app.loadLabel(pm).toString(),
                    packageName = app.packageName,
                    icon = app.loadIcon(pm)
                )
            }.sortedBy { it.name }
            _installedApps.value = filteredApps
            _isAppListLoading.value = false
        }
    }

    fun toggleGameApp(packageName: String) {
        viewModelScope.launch {
            val current = _gameApps.value.toMutableSet()
            if (current.contains(packageName)) current.remove(packageName)
            else current.add(packageName)
            repository.saveGameApps(current)
        }
    }

    fun toggleWhitelistApp(packageName: String) {
        viewModelScope.launch {
            val current = _killWhitelist.value.toMutableSet()
            if (current.contains(packageName)) current.remove(packageName)
            else current.add(packageName)
            repository.saveKillWhitelist(current)
        }
    }

    fun setZramEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                ShellManager.writeSysfs(SysfsPaths.ZRAM_RESET, "1")
                ShellManager.writeSysfs(SysfsPaths.ZRAM_DISKSIZE, _zramSize.value)
                Shell.cmd("mkswap /dev/block/zram0", "swapon /dev/block/zram0").exec()
            } else {
                Shell.cmd("swapoff /dev/block/zram0").exec()
                ShellManager.writeSysfs(SysfsPaths.ZRAM_RESET, "1")
            }
            repository.saveZramEnabled(enabled)
        }
    }

    fun setZramSize(size: String) {
        viewModelScope.launch {
            repository.saveZramSize(size)
            if (_zramEnabled.value) {
                setZramEnabled(false)
                setZramEnabled(true)
            }
        }
    }

    fun setChargePriority(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.CHARGE_PRIORITY, if (enabled) "1" else "0")) {
                repository.saveChargePriority(enabled)
            }
        }
    }

    fun setTcpCongestion(algo: String) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.TCP_CONGESTION, algo)) {
                repository.saveTcpCongestion(algo)
                _tcpCongestion.value = algo
            }
        }
    }

    fun setResolution(width: Int, height: Int) {
        viewModelScope.launch {
            Shell.cmd("wm size ${width}x${height}").exec()
        }
    }

    fun resetResolution() {
        viewModelScope.launch {
            Shell.cmd("wm size reset").exec()
        }
    }

    fun setDpi(density: Int) {
        viewModelScope.launch {
            Shell.cmd("wm density $density").exec()
            repository.saveDpiValue(density)
            refreshDpiInfo()
        }
    }

    fun resetDpi() {
        viewModelScope.launch {
            Shell.cmd("wm density reset").exec()
            repository.clearDpiValue()
            refreshDpiInfo()
        }
    }

    private fun refreshDpiInfo() {
        viewModelScope.launch {
            val result = Shell.cmd("wm density").exec()
            _dpiInfo.value = if (result.isSuccess) result.out.joinToString("\n") else "Error reading DPI"
        }
    }

    fun setTouchBoostEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.TOUCH_BOOST_ENABLED, if (enabled) "1" else "0")) {
                repository.saveTouchBoostEnabled(enabled)
            }
        }
    }

    fun setTouchBoostDuration(durationMs: Int) {
        viewModelScope.launch {
            val clamped = durationMs.coerceIn(20, 150)
            if (ShellManager.writeSysfs(SysfsPaths.TOUCH_BOOST_DURATION, clamped.toString())) {
                repository.saveTouchBoostDuration(clamped)
                _touchBoostDuration.value = clamped
            }
        }
    }

    fun setLmkAggressive(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.LMK_AGGRESSIVE, if (enabled) "1" else "0")) {
                repository.saveLmkAggressive(enabled)
                _lmkAggressive.value = enabled
            }
        }
    }

    private fun applySavedProfile() {
        viewModelScope.launch {
            _isApplyingProfile.value = true
            SettingsApplier.applyAll(getApplication<Application>())
            _isApplyingProfile.value = false
        }
    }

    private fun checkRoot() {
        viewModelScope.launch {
            _isRootAvailable.value = ShellManager.isRootAvailable()
        }
    }

    fun retryRoot() {
        checkRoot()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _kernelVersion.value = ShellManager.readSysfs(SysfsPaths.PROC_VERSION) ?: "Unknown"
            _currentGovernor.value = ShellManager.readSysfs(String.format(SysfsPaths.CPU_GOVERNOR_ALL, 0)) ?: "-"
            _availableGovernors.value = ShellManager.readSysfs(SysfsPaths.CPU_GOVERNOR_AVAILABLE)?.split(" ") ?: emptyList()
            _currentScheduler.value = parseScheduler(ShellManager.readSysfs(SysfsPaths.IO_SCHEDULER))
            
            val kcal = ShellManager.readSysfs(SysfsPaths.KCAL_CTRL) ?: "256 256 256"
            val parts = kcal.split(" ")
            if (parts.size >= 3) {
                _kcalR.value = parts[0].toFloatOrNull() ?: 256f
                _kcalG.value = parts[1].toFloatOrNull() ?: 256f
                _kcalB.value = parts[2].toFloatOrNull() ?: 256f
            }
            _kcalEnabled.value = (ShellManager.readSysfs(SysfsPaths.KCAL_ENABLE) ?: "0") == "1"
            _gpuConservative.value = (ShellManager.readSysfs(SysfsPaths.GPU_CONSERVATIVE) ?: "0") == "1"
            _fastCharge.value = (ShellManager.readSysfs(SysfsPaths.FAST_CHARGE) ?: "0") == "1"
            _autoFastChargeActive.value = (ShellManager.readSysfs(SysfsPaths.AUTO_FAST_CHARGE_STATUS) ?: "0") == "1"
            _tcpCongestion.value = ShellManager.readSysfs(SysfsPaths.TCP_CONGESTION) ?: "-"
            _availableTcpCongestions.value = ShellManager.readSysfs("/proc/sys/net/ipv4/tcp_available_congestion_control")?.split(" ") ?: listOf("cubic", "reno")
            _dynamicFsync.value = (ShellManager.readSysfs(SysfsPaths.DYNAMIC_FSYNC) ?: "0") == "1"
            
            refreshDpiInfo()
            _touchBoostEnabled.value = (ShellManager.readSysfs(SysfsPaths.TOUCH_BOOST_ENABLED) ?: "0") == "1"
            _touchBoostDuration.value = ShellManager.readSysfs(SysfsPaths.TOUCH_BOOST_DURATION)?.toIntOrNull() ?: 60
            _lmkAggressive.value = (ShellManager.readSysfs(SysfsPaths.LMK_AGGRESSIVE) ?: "0") == "1"
        }
    }

    fun setFastCharge(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.FAST_CHARGE, if (enabled) "1" else "0")) {
                _fastCharge.value = enabled
                repository.saveFastCharge(enabled)
            }
        }
    }

    fun setDynamicFsync(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.DYNAMIC_FSYNC, if (enabled) "1" else "0")) {
                _dynamicFsync.value = enabled
                repository.saveDynamicFsync(enabled)
            }
        }
    }

    fun setGpuConservative(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.GPU_CONSERVATIVE, if (enabled) "1" else "0")) {
                _gpuConservative.value = enabled
                repository.saveGpuConservative(enabled)
            }
        }
    }

    fun updateKcal(r: Float, g: Float, b: Float) {
        _kcalR.value = r
        _kcalG.value = g
        _kcalB.value = b
        applyKcal()
    }

    private fun applyKcal() {
        viewModelScope.launch {
            val kcalValue = "${_kcalR.value.toInt()} ${_kcalG.value.toInt()} ${_kcalB.value.toInt()}"
            if (ShellManager.writeSysfs(SysfsPaths.KCAL_CTRL, kcalValue)) {
                repository.saveKcalRgb(kcalValue)
            }
        }
    }

    fun setKcalEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.KCAL_ENABLE, if (enabled) "1" else "0")) {
                _kcalEnabled.value = enabled
                repository.saveKcalEnabled(enabled)
            }
        }
    }

    fun resetKcal() {
        updateKcal(256f, 256f, 256f)
    }

    fun compactZram() {
        viewModelScope.launch {
            ShellManager.writeSysfs(SysfsPaths.ZRAM_COMPACT, "1")
        }
    }

    private fun parseScheduler(raw: String?): String {
        if (raw == null) return "-"
        val regex = "\\[(.+)\\]".toRegex()
        val match = regex.find(raw)
        return match?.groupValues?.get(1) ?: raw.split(" ").firstOrNull() ?: "-"
    }

    fun setGovernor(gov: String) {
        viewModelScope.launch {
            val cpuCount = ShellManager.getCpuCount()
            var success = true
            for (i in 0 until cpuCount) {
                if (!ShellManager.writeSysfs(String.format(SysfsPaths.CPU_GOVERNOR_ALL, i), gov)) {
                    success = false
                }
            }
            if (success) {
                _currentGovernor.value = gov
                repository.saveCpuGovernor(gov)
            }
        }
    }

    fun setScheduler(sched: String) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.IO_SCHEDULER, sched)) {
                _currentScheduler.value = sched
                repository.saveIoScheduler(sched)
            }
        }
    }

    fun toggleGamingMode(enabled: Boolean) {
        viewModelScope.launch {
            _gamingMode.value = enabled
            repository.saveGamingMode(enabled)
            if (enabled) {
                setGovernor("performance")
                setScheduler("noop") // Or any gaming-preferred scheduler
            } else {
                setGovernor("interactive") // Or default
            }
        }
    }
}
