package ru.aston.dagger.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.aston.databases.NewsDatabase
import javax.inject.Singleton

@Module(
    includes = [
        ContextModule::class,
    ]
)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            NewsDatabase.DB_NAME
        ).build()
    }
}