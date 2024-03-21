package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.Ros
import com.github.thoebert.krosbridge.action.ActionFeedback
import kotlinx.serialization.Serializable

/*
{ "op": "action_feedback",
  "id": <string>,
  "action": <string>,
  "values": <json>
}
 */

@Serializable
data class FeedbackAction(
    override val id: String, val action: String,
    @Serializable(with = Ros.ActionFeedbackSerializer::class)
    val values: ActionFeedback? = null,
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "action_feedback"
    }
}