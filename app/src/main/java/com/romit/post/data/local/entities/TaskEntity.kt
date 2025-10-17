package com.romit.post.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String = "", // Unique ID of Document
    val title: String = "",
    val text: String = "",
    val isCompleted: Boolean = false,
    val userId: String = "", // The UID of the user who owns this todo
    val lastModified: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)