package com.github.thoebert.krosbridge

import kotlinx.serialization.Serializable

@Serializable
data class ActionResultStatus(
    val status: Short,
    @Serializable(with = Ros.ActionResultSerializer::class) val result: ActionResult? = null,
) : Message()
