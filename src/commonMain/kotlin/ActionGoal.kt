package com.github.thoebert.krosbridge

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class ActionGoal{
    @Transient val id: String = ""
}