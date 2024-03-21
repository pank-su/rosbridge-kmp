package com.github.thoebert.krosbridge.action

import com.github.thoebert.krosbridge.Ros
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlin.reflect.KClass

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

    suspend fun sendGoal(goal: In, feedback: Boolean): Flow<ActionType> =
        super.sendGoalGeneric(goal, feedback).transform {
            when (it) {
                is ActionType.ActionFeedback -> emit(it.copy(it.data as Feed))
                is ActionType.ActionResult -> {
                    emit(it.copy(it.data as Res))
                    //currentCoroutineContext().cancel(CancellationException("Get result"))
                }

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