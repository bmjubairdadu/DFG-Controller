package com.dfgcontroller.ui

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.dfgcontroller.ui.models.AppInfo
import androidx.lifecycle.AndroidViewModel
import com.dfgcontroller.core.UpdateManager
import com.dfgcontroller.core.UpdateManifest
import androidx.lifecycle.viewModelScope
import android.util.Log
import android.content.Context
import com.dfgcontroller.core.MemoryMonitor
import com.dfgcontroller.core.MemoryStats
import com.topjohnwu.superuser.Shell
import com.dfgcontroller.core.ShellManager
import com.dfgcontroller.core.SettingsApplier
import com.dfgcontroller.core.SysfsPaths
import com.dfgcontroller.data.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.seconds
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

    private val _currentCpuMinFreq = MutableStateFlow("-")
    val currentCpuMinFreq: StateFlow<String> = _currentCpuMinFreq.asStateFlow()

    private val _currentCpuMaxFreq = MutableStateFlow("-")
    val currentCpuMaxFreq: StateFlow<String> = _currentCpuMaxFreq.asStateFlow()

    private val _availableGovernors = MutableStateFlow<List<String>>(emptyList())
    val availableGovernors: StateFlow<List<String>> = _availableGovernors.asStateFlow()

    private val _availableFrequencies = MutableStateFlow<List<String>>(emptyList())
    val availableFrequencies: StateFlow<List<String>> = _availableFrequencies.asStateFlow()

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

    private val _thermalTemperature = MutableStateFlow("-")
    val thermalTemperature: StateFlow<String> = _thermalTemperature.asStateFlow()

    private val _isThermalCritical = MutableStateFlow(false)
    val isThermalCritical: StateFlow<Boolean> = _isThermalCritical.asStateFlow()

    private val _isLegacyKernel = MutableStateFlow(false)
    val isLegacyKernel: StateFlow<Boolean> = _isLegacyKernel.asStateFlow()

    private val _availableSchedulers = MutableStateFlow<List<String>>(emptyList())
    val availableSchedulers: StateFlow<List<String>> = _availableSchedulers.asStateFlow()

    private val _gamingChargeEnabled = MutableStateFlow(false)
    val gamingChargeEnabled: StateFlow<Boolean> = _gamingChargeEnabled.asStateFlow()

    private val _kernelLogs = MutableStateFlow<List<String>>(emptyList())
    val kernelLogs: StateFlow<List<String>> = _kernelLogs.asStateFlow()

    private val _currentProfileName = MutableStateFlow("Balanced")
    val currentProfileName: StateFlow<String> = _currentProfileName.asStateFlow()

    private val sysfsManager: com.dfgcontroller.core.SysfsManager = com.dfgcontroller.core.RootSysfsManager()

    private val profileManager = com.dfgcontroller.core.ProfileManager(sysfsManager)

    private val _integrityStatus = MutableStateFlow<IntegrityStatus>(IntegrityStatus.CHECKING)
    val integrityStatus: StateFlow<IntegrityStatus> = _integrityStatus.asStateFlow()

    private val _themeColorName = MutableStateFlow("Cyan")
    val themeColorName: StateFlow<String> = _themeColorName.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    enum class IntegrityStatus {
        CHECKING, VALID, INVALID_SIGNATURE, DEBUGGER_CONNECTED
    }

    private val _updateManifest = MutableStateFlow<UpdateManifest?>(null)
    val updateManifest: StateFlow<UpdateManifest?> = _updateManifest.asStateFlow()

    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus.asStateFlow()

    private val _kernelUpdateVariant = MutableStateFlow<com.dfgcontroller.core.KernelVariant?>(null)
    val kernelUpdateVariant: StateFlow<com.dfgcontroller.core.KernelVariant?> = _kernelUpdateVariant.asStateFlow()

    private val _kernelUpdateStatus = MutableStateFlow<String?>(null)
    val kernelUpdateStatus: StateFlow<String?> = _kernelUpdateStatus.asStateFlow()

    private val _isFirstRun = MutableStateFlow(false)
    val isFirstRun: StateFlow<Boolean> = _isFirstRun.asStateFlow()

    init {
        checkIntegrity()
        viewModelScope.launch {
            // Check for root availability first
            val root = ShellManager.isRootAvailable()
            _isRootAvailable.value = root
            
            if (root) {
                // Wait briefly for shell stability
                delay(1000)
                
                // Essential kernel capability check
                ShellManager.checkLegacyStatus()
                _isLegacyKernel.value = ShellManager.isLegacyKernelDetected()
                
                // Check if this is the first time the app is getting root
                val firstRun = repository.firstRun.first()
                _isFirstRun.value = firstRun
                
                // Read current kernel state WITHOUT writing anything
                loadInitialData()
                
                // SAFE START: Never auto-apply profiles on boot or startup in this build.
                // This ensures that if a setting caused a crash, the user can recover.
                Log.i("MainViewModel", "System initialized in Safe Mode. Automatic profile application disabled.")
                
                observeBypassSettings()
                observeNewSettings()
                startMemoryPolling()
                startLogPolling()
                checkForUpdates()
                checkForKernelUpdates()
            }
        }
    }

    fun refreshKernelStatus() {
        viewModelScope.launch {
            _isApplyingProfile.value = true
            ShellManager.checkLegacyStatus()
            _isLegacyKernel.value = ShellManager.isLegacyKernelDetected()
            loadInitialData()
            delay(500)
            _isApplyingProfile.value = false
            showStatus("Kernel status refreshed.")
        }
    }

    fun completeFirstRun() {
        viewModelScope.launch {
            repository.setFirstRunCompleted()
            _isFirstRun.value = false
        }
    }

    fun checkForKernelUpdates() {
        viewModelScope.launch {
            _kernelUpdateVariant.value = com.dfgcontroller.core.KernelUpdateManager.checkForUpdate()
        }
    }

    fun startKernelUpdate(context: Context) {
        _kernelUpdateVariant.value?.let { variant ->
            viewModelScope.launch {
                com.dfgcontroller.core.KernelUpdateManager.downloadKernel(context, variant) { status ->
                    _kernelUpdateStatus.value = status
                }
            }
        }
    }

    private fun startLogPolling() {
        viewModelScope.launch {
            while (true) {
                refreshKernelLogs()
                delay(10.seconds)
            }
        }
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
        viewModelScope.launch {
            repository.themeColor.collect { _themeColorName.value = it }
        }
    }

    private fun startMemoryPolling() {
        viewModelScope.launch {
            while (true) {
                _memoryStats.value = MemoryMonitor.getStats()
                _autoFastChargeActive.value = (ShellManager.readSysfs(SysfsPaths.AUTO_FAST_CHARGE_STATUS) ?: "0") == "1"
                
                val temp = ShellManager.readSysfs(SysfsPaths.THERMAL_ZONE) ?: 
                            ShellManager.readSysfs(SysfsPaths.THERMAL_STATUS) ?: "0"
                val tempInt = temp.replace("[^0-9.-]".toRegex(), "").split(".")[0].toIntOrNull() ?: 0
                val displayTemp = if (tempInt > 1000) tempInt / 1000 else if (tempInt < 0) 0 else tempInt
                _thermalTemperature.value = "$displayTemp°C"
                
                val critical = displayTemp > 60
                if (critical && !_isThermalCritical.value) {
                    _isThermalCritical.value = true
                    autoRevertSafety()
                } else {
                    _isThermalCritical.value = critical
                }

                delay(3.seconds)
            }
        }
    }

    private fun autoRevertSafety() {
        viewModelScope.launch {
            Log.w("MainViewModel", "Thermal safety triggered! Reverting to Balanced profile.")
            setKernelProfile(com.dfgcontroller.ui.models.KernelProfile.Balanced)
            showStatus("Thermal safety triggered! Performance throttled.")
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
            val filteredApps = apps.asSequence().filter { 
                (_showSystemApps.value || (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0)
            }.map { app ->
                AppInfo(
                    name = app.loadLabel(pm).toString(),
                    packageName = app.packageName,
                    icon = app.loadIcon(pm),
                )
            }.sortedBy { it.name }.toList()
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
            if (ShellManager.writeSysfs(SysfsPaths.DFG_TCP_CONGESTION, algo) ||
                ShellManager.writeSysfs(SysfsPaths.TCP_CONGESTION, algo)) {
                repository.saveTcpCongestion(algo)
                _tcpCongestion.value = algo
            }
        }
    }

    fun setResolution(width: Int, height: Int) {
        viewModelScope.launch {
            Shell.cmd("wm size ${width}x$height").exec()
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
                showStatus("Memory optimization ${if (enabled) "enabled" else "disabled"}")
            } else {
                showStatus("Failed to apply memory optimization")
            }
        }
    }

    fun setThemeColor(name: String) {
        viewModelScope.launch {
            repository.saveThemeColor(name)
        }
    }

    private fun showStatus(message: String) {
        viewModelScope.launch {
            _statusMessage.value = message
            delay(3.seconds)
            if (_statusMessage.value == message) {
                _statusMessage.value = null
            }
        }
    }

    private suspend fun applySavedProfile() {
        _isApplyingProfile.value = true
        SettingsApplier.applyAll(getApplication())
        _isApplyingProfile.value = false
    }

    private suspend fun checkRoot() {
        val root = ShellManager.isRootAvailable()
        _isRootAvailable.value = root
        if (root) {
            _isLegacyKernel.value = ShellManager.isLegacyKernelDetected()
            // Perform additional diagnostics
            val id = Shell.cmd("id").exec()
            Log.i("MainViewModel", "Root ID check: ${id.out.joinToString(" ")}")
            
            val dfgCheck = Shell.cmd("test -d ${SysfsPaths.DFG_BASE}").exec()
            if (dfgCheck.isSuccess) {
                Log.i("MainViewModel", "DFG Base directory found")
            } else {
                Log.w("MainViewModel", "DFG Base directory NOT found at ${SysfsPaths.DFG_BASE}")
            }
        }
    }

    fun retryRoot() {
        viewModelScope.launch {
            checkRoot()
        }
    }

    private suspend fun loadInitialData() {
        _kernelVersion.value = ShellManager.readSysfs(SysfsPaths.PROC_VERSION) ?: "Unknown"
        
        _currentGovernor.value = ShellManager.readSysfs(SysfsPaths.DFG_CPU_GOVERNOR, SysfsPaths.FALLBACK_GOVERNOR) ?: "-"
        
        val availableGovs = ShellManager.readSysfs("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors")
        _availableGovernors.value = availableGovs?.split(" ")?.filter { it.isNotBlank() } ?: emptyList()
        
        _currentCpuMinFreq.value = ShellManager.readSysfs(SysfsPaths.CPU_MIN_FREQ, SysfsPaths.FALLBACK_MIN_FREQ) ?: "-"
        _currentCpuMaxFreq.value = ShellManager.readSysfs(SysfsPaths.CPU_MAX_FREQ, SysfsPaths.FALLBACK_MAX_FREQ) ?: "-"
        
        _availableFrequencies.value = ShellManager.readSysfs(SysfsPaths.CPU_AVAILABLE_FREQ)?.split(" ")?.filter { it.isNotBlank() } ?: emptyList()

        val schedRaw = ShellManager.readSysfs(SysfsPaths.DFG_IO_SCHEDULER, SysfsPaths.IO_SCHEDULER)
        _currentScheduler.value = parseScheduler(schedRaw)
        
        val availableScheds = ShellManager.readSysfs(SysfsPaths.IO_SCHEDULER)
        _availableSchedulers.value = availableScheds?.replace("[", "")?.replace("]", "")?.split(" ")?.filter { it.isNotBlank() } ?: emptyList()

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
        _gamingChargeEnabled.value = (ShellManager.readSysfs(SysfsPaths.DFG_GAMING_CHARGE) ?: "0") == "1"
        
        _autoFastChargeActive.value = (ShellManager.readSysfs(SysfsPaths.AUTO_FAST_CHARGE_STATUS) ?: "0") == "1"
        _tcpCongestion.value = ShellManager.readSysfs(SysfsPaths.DFG_TCP_CONGESTION, SysfsPaths.TCP_CONGESTION) ?: "-"
        _availableTcpCongestions.value = ShellManager.readSysfs("/proc/sys/net/ipv4/tcp_available_congestion_control")?.split(" ") ?: listOf("cubic", "reno")
        _dynamicFsync.value = (ShellManager.readSysfs(SysfsPaths.DFG_DYN_FSYNC, SysfsPaths.DYNAMIC_FSYNC) ?: "0") == "1"
        
        refreshDpiInfo()
        _touchBoostEnabled.value = (ShellManager.readSysfs(SysfsPaths.TOUCH_BOOST_ENABLED) ?: "0") == "1"
        _touchBoostDuration.value = ShellManager.readSysfs(SysfsPaths.TOUCH_BOOST_DURATION)?.toIntOrNull() ?: 60
        _lmkAggressive.value = (ShellManager.readSysfs(SysfsPaths.LMK_AGGRESSIVE) ?: "0") == "1"
    }

    fun setFastCharge(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.FAST_CHARGE, if (enabled) "1" else "0")) {
                _fastCharge.value = enabled
                repository.saveFastCharge(enabled)
            }
        }
    }

    fun setGamingCharge(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.DFG_GAMING_CHARGE, if (enabled) "1" else "0")) {
                _gamingChargeEnabled.value = enabled
            }
        }
    }

    fun setDynamicFsync(enabled: Boolean) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.DFG_DYN_FSYNC, if (enabled) "1" else "0") ||
                ShellManager.writeSysfs(SysfsPaths.DYNAMIC_FSYNC, if (enabled) "1" else "0")) {
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
        val regex = "\\[(.+)]".toRegex()
        val match = regex.find(raw)
        return match?.groupValues?.get(1) ?: raw.split(" ").firstOrNull() ?: "-"
    }

    fun setCpuMinFreq(freq: String) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.CPU_MIN_FREQ, freq)) {
                _currentCpuMinFreq.value = freq
            }
        }
    }

    fun setCpuMaxFreq(freq: String) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.CPU_MAX_FREQ, freq)) {
                _currentCpuMaxFreq.value = freq
            }
        }
    }

    fun setGovernor(gov: String) {
        viewModelScope.launch {
            if (ShellManager.writeSysfs(SysfsPaths.DFG_CPU_GOVERNOR, gov)) {
                _currentGovernor.value = gov
                repository.saveCpuGovernor(gov)
                return@launch
            }
            
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
            if (ShellManager.writeSysfs(SysfsPaths.DFG_IO_SCHEDULER, sched) ||
                ShellManager.writeSysfs(SysfsPaths.IO_SCHEDULER, sched)) {
                _currentScheduler.value = sched
                repository.saveIoScheduler(sched)
            }
        }
    }

    fun refreshKernelLogs() {
        viewModelScope.launch {
            _kernelLogs.value = com.dfgcontroller.core.LogManager.getKernelLogs()
        }
    }

    fun exportLogs(context: Context, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val path = com.dfgcontroller.core.LogManager.exportLogs(context)
            onResult(path)
        }
    }

    fun setKernelProfile(profile: com.dfgcontroller.ui.models.KernelProfile) {
        if (_isThermalCritical.value && profile.name == "Performance") {
            // Block performance profile if overheating
            return
        }
        viewModelScope.launch {
            _isApplyingProfile.value = true
            if (profileManager.applyProfile(profile)) {
                _currentProfileName.value = profile.name
                _currentGovernor.value = profile.governor ?: _currentGovernor.value
                _currentScheduler.value = profile.ioScheduler ?: _currentScheduler.value
                repository.saveCurrentProfile(profile.name)
                repository.saveCpuGovernor(profile.governor ?: "")
                repository.saveIoScheduler(profile.ioScheduler ?: "")
            }
            _isApplyingProfile.value = false
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
