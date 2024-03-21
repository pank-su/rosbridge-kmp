package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionGoal
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
data class SendActionGoal<T>(
    val action: String,
    @SerialName("action_type") val actionType: String,
    val args: T? = null,
    val feedback: Boolean? = null,
    val fragment_size: Int? = null,
    val compression: String? = null,
    override val id: String? = null
) : ROSMessage(OPERATION) where T:ActionGoal{
    companion object {
        const val OPERATION = "send_action_goal"
    }
}