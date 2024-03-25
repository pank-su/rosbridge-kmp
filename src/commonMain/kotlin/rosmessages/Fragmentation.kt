package com.github.thoebert.krosbridge.rosmessages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/*
{ "op": "fragment",
  "id": <string>,
  "data": <string>,
  "num": <int>,
  "total": <int>
}
 */

@Serializable
data class Fragmentation  (
    @SerialName("id")
    private val _id: Int,
    val data: String,
    val num: Int? = null,
    val total : Double? = null,
): ROSMessage(OPERATION){

    @Transient
    override val id: String = _id.toString()

    companion object{
        const val OPERATION = "fragment"
    }
}