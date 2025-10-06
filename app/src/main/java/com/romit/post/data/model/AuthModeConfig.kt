package com.romit.post.data.model

import androidx.annotation.StringRes

data class AuthModeConfig(
    @field:StringRes val screenTitle: Int,
    @field:StringRes val mainButtonText: Int,
    @field:StringRes val bottomText: Int,
    @field:StringRes val bottomClickableText: Int,
    val onMainButtonClick: () -> Unit,
    val onSwitchModeClick: () -> Unit
)
