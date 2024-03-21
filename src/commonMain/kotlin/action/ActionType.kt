package com.github.thoebert.krosbridge.action

sealed interface ActionType {
    data class ActionResult(val data: com.github.thoebert.krosbridge.action.ActionResult?, val isResult: Boolean) :
        ActionType

    data class ActionFeedback(val data: com.github.thoebert.krosbridge.action.ActionFeedback?) : ActionType
}