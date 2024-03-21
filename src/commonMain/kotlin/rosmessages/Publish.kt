package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.Message
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/*
{ "op": "publish",
  (optional) "id": <string>,
  "topic": <string>,
  "msg": <json>
}
 */

@Serializable
data class Publish<T>(
    val topic: String,
    @Contextual
    val msg: T,
    override val id: String? = null,
) : ROSMessage(OPERATION) where T : Message {
    companion object {
        const val OPERATION = "publish"
    }
}