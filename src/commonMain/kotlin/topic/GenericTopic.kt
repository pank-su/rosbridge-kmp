package com.github.thoebert.krosbridge.topic

import com.github.thoebert.krosbridge.Ros
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlin.reflect.KClass

open class GenericTopic<M : Message>(
    override val ros: Ros,
    override var name: String,
    override val type: String,
    override val clz: KClass<out Message>,
) : Topic(ros, name, type, clz){

    suspend fun subscribe(handle : Any) : Flow<Pair<M?, String?>?> {
        return super.subscribeGeneric(handle).transform { pair -> emit(pair?.first as M? to pair?.second) }
    }

    suspend fun publish(message : M) {
        super.publishGeneric(message)
    }
}