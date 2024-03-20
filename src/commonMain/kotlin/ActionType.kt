package com.github.thoebert.krosbridge

sealed interface ActionType {
    data class ActionResult(val data: com.github.thoebert.krosbridge.ActionResult?, val isResult: Boolean) :
        ActionType

    data class ActionFeedback(val data: com.github.thoebert.krosbridge.ActionFeedback?) : ActionType
}