package com.github.thoebert.krosbridge.rosmessages

import com.github.thoebert.krosbridge.ServiceResponse
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/*
{ "op": "service_response",
  (optional) "id": <string>,
  "service": <string>,
  (optional) "values": <list<json>>,
  "result": <boolean>
}
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ResponseService<T> constructor(
    val service: String,
    val values: T? = null,
    @EncodeDefault
    val result: Boolean = true,
    override val id: String? = null,
) : ROSMessage(OPERATION) where T : ServiceResponse{
    companion object {
        const val OPERATION = "service_response"
    }
}