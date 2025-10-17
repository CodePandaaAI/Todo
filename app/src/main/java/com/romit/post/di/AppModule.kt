package com.romit.post.di

import android.content.Context
import androidx.room.Room
import com.romit.post.data.local.dao.TaskDao
import com.romit.post.data.local.database.AppDatabase
import com.romit.post.data.repositories.TodoRepository
import com.romit.post.data.repositories.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            AppDatabase::class.java,
            "Todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideRepository(taskDao: TaskDao): TodoRepository {
        return TodoRepositoryImpl(taskDao)
    }
}