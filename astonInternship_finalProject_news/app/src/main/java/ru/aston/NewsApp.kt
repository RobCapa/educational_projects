package ru.aston

import android.app.Application
import ru.aston.dagger.DaggerAppComponent

class NewsApp : Application() {
    val appComponent = DaggerAppComponent
        .builder()
        .application(this)
        .build()
}