package com.github.thoebert.krosbridge.service

import com.github.thoebert.krosbridge.Ros
import kotlin.reflect.KClass

open class GenericService<In : ServiceRequest, Out : ServiceResponse>(
    override val ros: Ros,
    override val name: String,
    override val type: String,
    override val requestClz: KClass<out ServiceRequest>,
    override val responseClz: KClass<out ServiceResponse>,
) : Service(ros, name, type, requestClz, responseClz){
    suspend fun call(input : In) : Pair<Out?, Boolean> {
        val (resp, result) = super.callService(input)
        val respCasted = resp as Out?
        return respCasted to result
    }

    suspend fun advertiseService(callback: (In?, String?) -> Unit) {
        return super.advertiseServiceGeneric { m, id ->
            callback(m as In?, id)
        }
    }

    suspend fun sendResponse(response: Out?, result : Boolean = true, id: String? = null) {
        super.sendResponseGeneric(response, result, id)
    }
}