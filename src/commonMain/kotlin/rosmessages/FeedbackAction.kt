package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionFeedback
import kotlinx.serialization.Serializable

/*
{ "op": "action_feedback",
  "id": <string>,
  "action": <string>,
  "values": <json>
}
 */

@Serializable
data class FeedbackAction<T>(
    override val id: String, val action: String,
    val values: T? = null,
) : ROSMessage(OPERATION) where T: ActionFeedback {
    companion object {
        const val OPERATION = "action_feedback"
    }
}