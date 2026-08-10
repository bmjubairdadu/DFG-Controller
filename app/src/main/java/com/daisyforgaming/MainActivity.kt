package com.daisyforgaming

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.daisyforgaming.core.BypassChargingService
import com.daisyforgaming.core.GameModeService
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.screens.*
import com.daisyforgaming.ui.theme.DFGControllerTheme
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkBackground
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object CpuIo : Screen("cpuio", "CPU & I/O", Icons.Default.Settings)
    object Display : Screen("display", "Display", Icons.Default.Edit)
    object Gpu : Screen("gpu", "GPU", Icons.Default.Build)
    object Games : Screen("games", "Games", Icons.Default.PlayArrow)
    object Power : Screen("power", "Power", Icons.Default.Refresh)
    object AppSelector : Screen("app_selector", "App Selector", Icons.Default.List)
    object About : Screen("about", "About", Icons.Default.Info)
    object Compatibility : Screen("compatibility", "Compatibility", Icons.Default.CheckCircle)
    object Wakelocks : Screen("wakelocks", "Wakelocks", Icons.Default.BatteryAlert)
    object Packages : Screen("packages", "Packages", Icons.AutoMirrored.Filled.ListAlt)
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        observeServiceState()
        
        setContent {
            DFGControllerTheme {
                val integrityStatus by viewModel.integrityStatus.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    when (integrityStatus) {
                        MainViewModel.IntegrityStatus.CHECKING -> { /* Loading */ }
                        MainViewModel.IntegrityStatus.VALID -> {
                            val isRootAvailable by viewModel.isRootAvailable.collectAsState()
                            when (isRootAvailable) {
                                null -> { /* Loading state */ }
                                false -> RootRequiredScreen(onRetry = { viewModel.retryRoot() })
                                true -> MainScaffold(viewModel)
                            }
                        }
                        MainViewModel.IntegrityStatus.INVALID_SIGNATURE -> IntegrityErrorScreen("App signature verification failed. This APK may have been tampered with.")
                        MainViewModel.IntegrityStatus.DEBUGGER_CONNECTED -> IntegrityErrorScreen("Debugger detected. Please disconnect the debugger to use this app.")
                    }
                }
            }
        }
    }

    private fun observeServiceState() {
        lifecycleScope.launch {
            combine(viewModel.bypassTriggerEnabled, viewModel.gameApps) { bypass, games ->
                bypass to games.isNotEmpty()
            }.collect { (bypassEnabled, gamesModeActive) ->
                val bypassIntent = Intent(this@MainActivity, BypassChargingService::class.java)
                if (bypassEnabled) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) startForegroundService(bypassIntent)
                    else startService(bypassIntent)
                } else stopService(bypassIntent)

                val gameIntent = Intent(this@MainActivity, GameModeService::class.java)
                if (gamesModeActive) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) startForegroundService(gameIntent)
                    else startService(gameIntent)
                } else stopService(gameIntent)
            }
        }
    }
}

@Composable
fun IntegrityErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Text("INTEGRITY FAILURE", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, textAlign = TextAlign.Center, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Dashboard,
        Screen.CpuIo,
        Screen.Display,
        Screen.Gpu,
        Screen.Games,
        Screen.Power
    )

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val isMainScreen = screens.any { it.route == currentDestination?.route }
            
            if (isMainScreen) {
                TopAppBar(
                    title = { 
                        Text(
                            "DFG CONTROLLER", 
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = com.daisyforgaming.ui.theme.OrbitronFamily,
                            color = ElectricCyan
                        ) 
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                            Icon(Icons.Default.Info, contentDescription = "About", tint = Color.Gray)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
                )
            }
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = currentDestination?.route != Screen.AppSelector.route && 
                               currentDestination?.route != Screen.About.route &&
                               currentDestination?.route != Screen.Compatibility.route

            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkBackground,
                    tonalElevation = 8.dp
                ) {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricCyan,
                                selectedTextColor = ElectricCyan,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = ElectricCyan.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            exitTransition = { slideOutHorizontally { -it } + fadeOut() },
            popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
            composable(Screen.CpuIo.route) { CpuIoScreen(viewModel) }
            composable(Screen.Display.route) { DisplayScreen(viewModel) }
            composable(Screen.Gpu.route) { GpuScreen(viewModel) }
            composable(Screen.Games.route) {
                GamesScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(Screen.Power.route) { 
                PowerScreen(
                    viewModel = viewModel,
                    onNavigateToAppSelector = { navController.navigate(Screen.AppSelector.route) },
                    onNavigateToWakelocks = { navController.navigate(Screen.Wakelocks.route) }
                ) 
            }
            composable(Screen.AppSelector.route) { 
                AppSelectorScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                ) 
            }
            composable(Screen.About.route) {
                AboutScreen(
                    onNavigateToCompatibility = { navController.navigate(Screen.Compatibility.route) },
                    onNavigateToPackages = { navController.navigate(Screen.Packages.route) }
                )
            }
            composable(Screen.Compatibility.route) {
                CompatibilityScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Wakelocks.route) {
                WakelockScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Packages.route) {
                PackagesScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
