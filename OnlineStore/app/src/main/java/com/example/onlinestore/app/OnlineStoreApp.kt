package com.example.onlinestore.app

import android.app.Application
import com.example.onlinestore.di.DaggerAppComponent

class OnlineStoreApp : Application() {

    val appComponent = DaggerAppComponent
        .builder()
        .application(this)
        .build()

}