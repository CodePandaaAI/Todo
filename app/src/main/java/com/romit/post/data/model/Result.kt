package com.romit.post.data.model

sealed class TodoResult<out T> {
    /**
     * Represents a successful data retrieval.
     * @param data The data that was retrieved.
     */
    data class Success<out T>(val data: T) : TodoResult<T>()

    /**
     * Represents a failed data retrieval.
     * @param exception The exception that occurred.
     */
    data class Failure(val exception: Exception) : TodoResult<Nothing>()

    /**
     * Represents the data is currently being loaded.
     */
    object Loading : TodoResult<Nothing>()
}