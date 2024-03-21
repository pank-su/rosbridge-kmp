package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.Ros
import com.github.thoebert.krosbridge.action.ActionGoal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
{ "op": "send_action_goal",
  (optional) "id": <string>,
  "action": <string>,
  "action_type": <string>,
  (optional) "args": <list<json>>,
  (optional) "feedback": <boolean>,
  (optional) "fragment_size": <int>,
  (optional) "compression": <string>
}
 */

@Serializable
data class SendActionGoal(
    val action: String,
    @SerialName("action_type") val actionType: String,
    @Serializable(with = Ros.ActionSendGoalSerializer::class)
    val args: ActionGoal? = null,
    val feedback: Boolean? = null,
    val fragment_size: Int? = null,
    val compression: String? = null,
    override val id: String? = null
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "send_action_goal"
    }
}