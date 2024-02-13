package com.example.onlinestore.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppContextModule {

    @Singleton
    @Provides
    fun context(app: Application): Context {
        return app.applicationContext
    }
}