package com.romit.post.data.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.romit.post.data.model.Task
import com.romit.post.data.model.TodoResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface TodoRepository {
    suspend fun addTodo(task: Task)
    suspend fun updateTodo(taskId: String, newTitle: String, newText: String)
    suspend fun deleteTodo(taskId: String)
    fun getTodos(): Flow<TodoResult<List<Task>>>
}

class TodoRepositoryImpl : TodoRepository {
    private val firestore = Firebase.firestore

    private val currentUser = Firebase.auth.currentUser

    override suspend fun addTodo(task: Task) {
        firestore.collection("todos").add(task).await()
    }

    override suspend fun updateTodo(taskId: String, newTitle: String, newText: String) {
        if (currentUser == null) return

        // Creating a map of the fields we want to update
        val updates = mapOf("title" to newTitle, "text" to newText)

        firestore.collection("todos").document(taskId).update(updates).await()
    }

    override suspend fun deleteTodo(taskId: String) {
        firestore.collection("todos").document(taskId).delete().await()
    }


    override fun getTodos(): Flow<TodoResult<List<Task>>> = callbackFlow {
        // 1. Getting a reference to the Firestore query
        val query = firestore.collection("todos")
            .whereEqualTo("userId", currentUser?.uid)

        // 2. Attaching the snapshot listener
        val listener = query.addSnapshotListener { snapshot, error ->
            // 3. Inside the callback, handle results and send them to the Flow
            if (error != null) {
                // If there's an error, send a Failure result
                trySend(TodoResult.Failure(error))
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // If we get a snapshot, convert it and send a Success result
                val tasks = snapshot.toObjects(Task::class.java)
                trySend(TodoResult.Success(tasks))
            }
        }

        awaitClose {
            listener.remove()
        }
    }
}