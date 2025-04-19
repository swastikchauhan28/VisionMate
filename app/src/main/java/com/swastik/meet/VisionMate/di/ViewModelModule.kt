package com.swastik.meet.VisionMate.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.swastik.meet.VisionMate.viewmodels.*

val viewmodelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::ToolViewModel)
}