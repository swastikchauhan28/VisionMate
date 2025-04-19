package com.swastik.meet.VisionMate.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.swastik.meet.VisionMate.util.Tools
import kotlinx.coroutines.flow.*



class MainViewModel : ViewModel() {


    private val _isLoadingStateFlow = MutableStateFlow(false)
    val isLoadingStateFlow = _isLoadingStateFlow.asStateFlow()


    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    private val _currentTools = MutableStateFlow(Tools.DescribeImage)
    val currentTools = _currentTools.asStateFlow()

    private val _currentPrompt = MutableStateFlow("")
    val currentPrompt = _currentPrompt.asStateFlow()

    fun onToolsSelected(tools: Tools) {
        _currentTools.update {
            tools
        }
    }

    fun setCurrentPrompt(prompt: String) {
        _currentPrompt.update {
            prompt
        }
    }

    fun onTakePhoto(bitmap: Bitmap?) {
        _bitmaps.update {
            bitmap
        }
    }

    fun setLoading(value: Boolean) {
        _isLoadingStateFlow.update {
            value
        }
    }
}