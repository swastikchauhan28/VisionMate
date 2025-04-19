package com.swastik.meet.VisionMate.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swastik.meet.VisionMate.ui.common.LoadingDialog
import com.swastik.meet.VisionMate.ui.screens.camera.CameraScreen
import com.swastik.meet.VisionMate.ui.screens.instruction.InstructionScreen
import com.swastik.meet.VisionMate.ui.screens.home.HomeScreen
import com.swastik.meet.VisionMate.ui.screens.prompt_write.PromptScreen
import com.swastik.meet.VisionMate.ui.screens.result.ResultScreen
import com.swastik.meet.VisionMate.util.FadeIn
import com.swastik.meet.VisionMate.util.FadeOut
import com.swastik.meet.VisionMate.viewmodels.MainViewModel
import com.swastik.meet.VisionMate.viewmodels.ToolViewModel


val LocalNavControllerProvider: ProvidableCompositionLocal<NavHostController> = staticCompositionLocalOf {
    error("No Nav Controller provided")
}

@Composable
fun NavGraph(activity: Activity) {
    val navController = rememberNavController()
    val mainViewModel = viewModel<MainViewModel>()
    val toolViewModel = viewModel<ToolViewModel>()
    val isLoading by mainViewModel.isLoadingStateFlow.collectAsState()
    LoadingDialog(isLoading)
    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NavHost(
            navController = navController,
            startDestination = RouteScreen.Home.route,
            enterTransition = { FadeIn },
            exitTransition = { FadeOut },
        ) {
            composable(route = RouteScreen.Home.route) {
                HomeScreen(mainViewModel = mainViewModel,activity)
            }
            composable(route = RouteScreen.Instruction.route) {
                InstructionScreen()
            }
            composable(route = RouteScreen.Prompt.route) {
                PromptScreen(mainViewModel = mainViewModel)
            }
            composable(route = RouteScreen.Camera.route) {
                CameraScreen(mainViewModel = mainViewModel)
            }
            composable(route = RouteScreen.Result.route) {
                ResultScreen(mainViewModel = mainViewModel, toolViewModel = toolViewModel)
            }
        }
    }
}
