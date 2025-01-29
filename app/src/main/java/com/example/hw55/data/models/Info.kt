package com.example.hw55.data.models


import kotlinx.serialization.SerialName

@Serializable
data class Info(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("pages")
    val pages: Int? = null,
    @SerialName("next")
    val next: String? = null,
    @SerialName("prev")
    val prev: Any? = null
)