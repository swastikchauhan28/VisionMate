package com.swastik.meet.VisionMate.ui.screens.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.swastik.meet.VisionMate.R
import com.swastik.meet.VisionMate.ui.navigation.LocalNavControllerProvider
import com.swastik.meet.VisionMate.ui.navigation.RouteScreen
import com.swastik.meet.VisionMate.util.Tools
import com.swastik.meet.VisionMate.util.addToastSpeech
import com.swastik.meet.VisionMate.util.detectSwipe
import com.swastik.meet.VisionMate.util.pauseVoice
import com.swastik.meet.VisionMate.util.showToast
import com.swastik.meet.VisionMate.viewmodels.MainViewModel



@Composable
fun CameraScreen(mainViewModel: MainViewModel) {
    val navController = LocalNavControllerProvider.current
    val currentTools by mainViewModel.currentTools.collectAsState()
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        showToast(R.string.swipe_left_to_capture_image)
        addToastSpeech(R.string.swipe_right_to_home_screen)
    }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectSwipe(
                    onSwipeLeft = {
                        mainViewModel.setLoading(true)
                        takePhoto(
                            context = context,
                            controller = controller,
                            onPhotoTaken = {
                                pauseVoice()
                                mainViewModel.onTakePhoto(it)
                                mainViewModel.setLoading(false)
                                navController.navigate(RouteScreen.Result.route) {
                                    if (currentTools == Tools.DescribeImage || currentTools == Tools.ImageToText) {
                                        popUpTo(RouteScreen.Camera.route) { inclusive = true }
                                    } else {
                                        popUpTo(RouteScreen.Camera.route) { inclusive = true }
                                        popUpTo(RouteScreen.Prompt.route) { inclusive = true }
                                    }
                                }
                            }
                        )
                    },
                    onSwipeRight = {
                        pauseVoice()
                        navController.navigateUp()
                    },
                )
            }
    )
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}
