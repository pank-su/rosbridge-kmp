package com.github.thoebert.krosbridge

import kotlinx.serialization.Serializable

@Serializable
data class ActionResultStatus<T: ActionResult>(
    val status: Short,
     val result: T? = null,
) : Message()
