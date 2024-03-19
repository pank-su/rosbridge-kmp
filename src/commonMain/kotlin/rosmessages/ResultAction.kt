package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionResult
import com.github.thoebert.krosbridge.Ros
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
    @Serializable(with = Ros.ActionResultSerializer::class) val values: ActionResult? = null,
    val result: Boolean
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "action_result"
    }
}
