package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.action.ActionResultStatus
import kotlinx.serialization.Serializable

/*
{ "op": "action_result",
  "id": <string>,
  "action": <string>,
  "values": <json>,
  "result": <boolean>
}
 */

@Serializable
data class ResultAction(
    override val id: String,
    val action: String,
    val values: ActionResultStatus,
    val result: Boolean
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "action_result"
    }
}
