package com.github.thoebert.krosbridge

import com.github.thoebert.krosbridge.rosmessages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass


typealias ActionGoalSubscriber = (ActionGoal?, String?) -> Unit
typealias ActionResultSubscriber = (ActionType, String?) -> Unit

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
    private val resultSubscribers = mutableMapOf<String, ActionResultSubscriber>()

    suspend fun sendGoalGeneric(goal: ActionGoal, feedback: Boolean = false, callback: ActionResultSubscriber) {
        val actionId = "call_action:$name:${ros.nextId()}"
        resultSubscribers[actionId] = callback
        ros.registerAction(this)
        ros.send(SendActionGoal(name, type, goal, id = actionId, feedback = feedback))
    }

    internal fun receivedFeedback(feedback: ActionFeedback?, id: String?) {
        resultSubscribers[id]?.let { it(ActionType.ActionFeedback(feedback), id) }
    }

    internal fun receivedResult(result: ActionResult?, isResult: Boolean, id: String?) {
        resultSubscribers[id]?.let { it(ActionType.ActionResult(result, isResult), id) }
        ros.deregisterAction(this)
        resultSubscribers.remove(id)
    }

    internal fun receivedGoal(goal: ActionGoal?, isFeedback: Boolean?, id: String?) {
        goalSubscriber?.let { it(goal, id) }
    }

    suspend fun advertiseActionGeneric(callback: ActionGoalSubscriber?) {
        goalSubscriber = callback
        ros.registerAction(this)
        ros.send(AdvertiseAction(name, type))
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
        ros.send(ResultAction(id, name, result, isResult))

    }

    suspend fun sendGoalGeneric(goal: ActionGoal): ActionType {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.Default).launch {
                sendGoalGeneric(goal) { type, _ ->
                    continuation.resume(type)
                }
            }
        }
    }

}

open class GenericAction<In : ActionGoal, Feed : ActionFeedback, Res : ActionResult>(
    override val ros: Ros,
    override val name: String,
    override val type: String,
    override val goalClz: KClass<out ActionGoal>,
    override val feedbackClz: KClass<out ActionFeedback>,
    override val resultClz: KClass<out ActionResult>,

    ) : Action(ros, name, type, goalClz, feedbackClz, resultClz) {

    suspend fun advertiseAction(callback: (In?, String?) -> Unit) {
        return super.advertiseActionGeneric { goal, id ->
            callback(goal as In?, id)
        }
    }

    suspend fun sendGoal(goal: In): ActionType = super.sendGoalGeneric(goal).apply {
        when (this) {
            is ActionType.ActionFeedback -> this.data as Feed
            is ActionType.ActionResult -> this.data as Res
            else -> {}
        }
    }


    suspend fun sendFeedback(feedback: Feed?, id: String) {
        super.sendFeedbackGeneric(feedback, id)
    }

    suspend fun sendResult(result: Res?, id: String, isResult: Boolean = true) {
        super.sendResult(result, isResult, id)
    }
}