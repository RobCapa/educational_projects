package com.example.onlinestore.di.modules

import android.content.Context
import androidx.room.Room
import com.example.data.database.OnlineStoreDatabase
import com.example.data.database.dao.FavoriteProductDao
import com.example.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        AppContextModule::class,
    ]
)
class DatabaseModule {

    @Provides
    fun provideUserDao(database: OnlineStoreDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideFavoriteProductDao(database: OnlineStoreDatabase): FavoriteProductDao {
        return database.favoriteProductDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): OnlineStoreDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = OnlineStoreDatabase::class.java,
            name = OnlineStoreDatabase.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }
}