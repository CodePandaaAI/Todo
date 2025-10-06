package com.romit.post.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object TodoScreen: Screen
}