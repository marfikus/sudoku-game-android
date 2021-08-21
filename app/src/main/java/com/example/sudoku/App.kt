package com.example.sudoku

import android.app.Application

class App : Application() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()

        mainViewModel = MainViewModel()
    }
}