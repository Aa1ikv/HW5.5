package com.example.hw55.data.models


import kotlinx.serialization.SerialName

@Serializable
data class BaseResponce(
    @SerialName("info")
    val info: Info? = null,
    @SerialName("results")
    val results: List<Result?>? = null
)