package com.romit.post.data.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId
    val id: String = "", // Unique ID of Document
    val title: String = "",
    val text: String = "",
    val isCompleted: Boolean = false,
    val userId: String = "" // The UID of the user who owns this todo
)