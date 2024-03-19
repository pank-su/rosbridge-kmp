package com.github.thoebert.krosbridge.rosmessages

import kotlinx.serialization.Serializable

/*
{ "op": "advertise_action",
  "type": <string>,
  "action": <string>
}
 */

@Serializable
data class AdvertiseAction(
    val type: String,
    val action: String, override val id: String? = null
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "advertise_action"
    }
}
