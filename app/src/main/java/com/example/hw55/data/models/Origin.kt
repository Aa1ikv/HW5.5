package com.example.hw55.data.models


import kotlinx.serialization.SerialName

@Serializable
data class Origin(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)