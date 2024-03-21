package com.github.thoebert.krosbridge.rosmessages

import kotlinx.serialization.Serializable

/*
{ "op": "unadvertise_action",
  "action": <string>
}
 */

@Serializable
data class UnadvertiseAction(
    val action: String, override val id: String? = null
) : ROSMessage(OPERATION) {
    companion object {
        const val OPERATION = "unadvertise_action"
    }
}
