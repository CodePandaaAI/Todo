package com.romit.post.data

import com.google.firebase.firestore.DocumentId

data class Todo(
    @DocumentId
    val id: String = "", // Unique ID of Document
    val text: String = "",
    val isCompleted: Boolean = false,
    val userId: String = "" // The UID of the user who owns this todo
)