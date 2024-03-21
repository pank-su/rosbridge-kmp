package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ActionResult
import com.github.thoebert.krosbridge.ActionResultStatus
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
data class ResultAction<T>(
    override val id: String,
    val action: String,
    val values: ActionResultStatus<T>,
    val result: Boolean
) : ROSMessage(OPERATION) where T : ActionResult{
    companion object {
        const val OPERATION = "action_result"
    }
}
