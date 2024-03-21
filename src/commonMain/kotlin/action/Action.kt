package com.github.thoebert.krosbridge.action

import com.github.thoebert.krosbridge.Ros
import com.github.thoebert.krosbridge.rosmessages.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass


typealias ActionGoalSubscriber = (ActionGoal?, String?) -> Unit

open class Action(
    open val ros: Ros, open val name: String,
    open val type: String,
    open val goalClz: KClass<out ActionGoal>,
    open val feedbackClz: KClass<out ActionFeedback>,
    open val resultClz: KClass<out ActionResult>,

    ) {
    val isAdvertised: Boolean
        get() = goalSubscriber != null

    private var goalSubscriber: ActionGoalSubscriber? = null
    private val resultSubscribers = mutableMapOf<String, MutableStateFlow<ActionType?>>()

    suspend fun sendGoalGeneric(
        goal: ActionGoal,
        feedback: Boolean = false/*, flow: StateFlow<ActionResult>*/
    ): Flow<ActionType?> {
        val actionId = "call_action:$name:${ros.nextId()}"
        resultSubscribers[actionId] = MutableStateFlow(null)
        ros.registerAction(this)
        ros.send(SendActionGoal(name, type, goal, id = actionId, feedback = feedback))
        return resultSubscribers[actionId]!!.asStateFlow()
    }

    internal fun receivedFeedback(feedback: ActionFeedback?, id: String?) {
        resultSubscribers[id]!!
        resultSubscribers[id]?.let { it.value = ActionType.ActionFeedback(feedback) }
    }

    internal fun receivedResult(result: ActionResult?, isResult: Boolean, id: String?) {
        resultSubscribers[id]?.let {
            it.value = ActionType.ActionResult(result, isResult)
        }
        ros.deregisterAction(this)

        resultSubscribers.remove(id)
    }

    internal fun receivedGoal(goal: ActionGoal?, isFeedback: Boolean?, id: String?) {
        goalSubscriber?.let { it(goal, id) }
    }

    suspend fun advertiseActionGeneric(callback: ActionGoalSubscriber?) {
        goalSubscriber = callback
        ros.registerAction(this)
        ros.send(AdvertiseAction(type, name))
    }

    suspend fun unadvertiseAction() {
        ros.deregisterAction(this)
        ros.send(UnadvertiseAction(name))
        goalSubscriber = null
    }

    suspend fun sendFeedbackGeneric(feedback: ActionFeedback?, id: String) {
        ros.send(FeedbackAction(id, name, feedback))
    }

    suspend fun sendResult(result: ActionResult?, isResult: Boolean, id: String) {
        ros.send(ResultAction2(id, name,  result, isResult))
    }

}


