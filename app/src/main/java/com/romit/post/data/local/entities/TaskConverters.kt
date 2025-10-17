package com.romit.post.data.local.entities

import com.romit.post.data.model.Task

// Convert Firestore Task to Room TaskEntity
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        text = this.text,
        isCompleted = this.isCompleted,
        userId = this.userId,
        lastModified = System.currentTimeMillis(),
        isSynced = true // Coming from Firestore, so it's synced
    )
}

// Convert Room TaskEntity to Firestore Task
fun TaskEntity.toTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        text = this.text,
        isCompleted = this.isCompleted,
        userId = this.userId
    )
}