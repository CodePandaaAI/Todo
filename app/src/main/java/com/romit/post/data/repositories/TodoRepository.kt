package com.romit.post.data.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.romit.post.data.local.dao.TaskDao
import com.romit.post.data.local.entities.toEntity
import com.romit.post.data.local.entities.toTask
import com.romit.post.data.model.Task
import com.romit.post.data.model.TodoResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface TodoRepository {
    fun getTodos(): Flow<TodoResult<List<Task>>>
    suspend fun addTodo(task: Task)
    suspend fun updateTodo(taskId: String, newTitle: String, newText: String)
    suspend fun deleteTodo(taskId: String)
}

@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TodoRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getTodos(): Flow<TodoResult<List<Task>>> {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            return flow {
                emit(TodoResult.Failure(Exception("User not authenticated")))
            }
        }

        syncFromFirestore(userId)

        return taskDao.getTasksByUserId(userId).map { entities ->
            try {
                TodoResult.Success(entities.map { it.toTask() })
            } catch (e: Exception) {
                TodoResult.Failure(e)
            }
        }
    }

    private fun syncFromFirestore(userId: String) {
        firestore.collection("todos")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                snapshot?.documents?.let { docs ->
                    val tasks = docs.mapNotNull { it.toObject(Task::class.java) }
                    repositoryScope.launch {
                        taskDao.upsertTasks(tasks.map { it.toEntity() })
                    }
                }
            }
    }

    override suspend fun addTodo(task: Task) {
        val userId = auth.currentUser?.uid
            ?: throw Exception("User not authenticated")

        val docRef = firestore.collection("todos").document()
        val taskWithId = task.copy(id = docRef.id, userId = userId)

        // Save locally first
        taskDao.upsertTask(taskWithId.toEntity().copy(isSynced = false))

        // Sync to Firestore
        try {
            docRef.set(taskWithId).await()
            taskDao.upsertTask(taskWithId.toEntity().copy(isSynced = true))
        } catch (e: Exception) {
            // Stays unsynced, will retry later
        }
    }

    override suspend fun updateTodo(taskId: String, newTitle: String, newText: String) {
        val existingTask = taskDao.getTaskById(taskId) ?: return

        val updatedTask = existingTask.copy(
            title = newTitle,
            text = newText,
            lastModified = System.currentTimeMillis(),
            isSynced = false
        )

        taskDao.upsertTask(updatedTask)

        try {
            firestore.collection("todos")
                .document(taskId)
                .update(mapOf("title" to newTitle, "text" to newText))
                .await()

            taskDao.upsertTask(updatedTask.copy(isSynced = true))
        } catch (e: Exception) {
            // Stays unsynced
        }
    }

    override suspend fun deleteTodo(taskId: String) {
        taskDao.deleteTaskById(taskId)

        try {
            firestore.collection("todos")
                .document(taskId)
                .delete()
                .await()
        } catch (e: Exception) {
            // Already deleted locally
        }
    }
}