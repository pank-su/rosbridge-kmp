package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionResult
import com.github.thoebert.krosbridge.Ros
import kotlinx.serialization.Serializable

@Serializable
data class ResultAction2(
    override val id: String,
    val action: String,
    @Serializable(with = Ros.ActionResultSerializer::class) val values: ActionResult?,
    val result: Boolean
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "action_result"
    }
}