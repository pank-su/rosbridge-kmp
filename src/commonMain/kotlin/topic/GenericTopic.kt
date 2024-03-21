package com.github.thoebert.krosbridge.topic

import com.github.thoebert.krosbridge.Ros
import kotlin.reflect.KClass

open class GenericTopic<M : Message>(
    override val ros: Ros,
    override var name: String,
    override val type: String,
    override val clz: KClass<out Message>,
) : Topic(ros, name, type, clz){

    suspend fun subscribe(handle : Any, callback: (M, String?) -> Unit) : Boolean {
        return super.subscribeGeneric(handle) { m, id ->
            callback(m as M, id)
        }
    }

    suspend fun publish(message : M) {
        super.publishGeneric(message)
    }
}