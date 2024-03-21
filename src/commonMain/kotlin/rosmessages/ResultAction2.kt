package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionResult
import kotlinx.serialization.Serializable

@Serializable
data class ResultAction2<T>(
    override val id: String,
    val action: String,
    val values: T?,
    val result: Boolean
) : ROSMessage(OPERATION) where T : ActionResult {
    companion object {
        const val OPERATION = "action_result"
    }
}