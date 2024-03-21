package com.github.thoebert.krosbridge.action

import com.github.thoebert.krosbridge.Ros
import com.github.thoebert.krosbridge.topic.Message
import kotlinx.serialization.Serializable

@Serializable
data class ActionResultStatus(
    val status: Short,
    @Serializable(with = Ros.ActionResultSerializer::class) val result: ActionResult? = null,
) : Message()
