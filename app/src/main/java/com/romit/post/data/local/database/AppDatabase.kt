package com.romit.post.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romit.post.data.local.dao.TaskDao
import com.romit.post.data.local.entities.TaskEntity


@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}