package com.marfikus.sudoku

import android.app.Application

class App : Application() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()

        mainViewModel = MainViewModel(
            Game(),
            AppSettings(applicationContext)
        )
        mainViewModel.init()
    }
}