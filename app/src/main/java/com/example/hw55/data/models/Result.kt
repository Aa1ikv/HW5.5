package com.example.hw55.data.models


import kotlinx.serialization.SerialName

@Serializable
data class Result(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("species")
    val species: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("origin")
    val origin: Origin? = null,
    @SerialName("location")
    val location: Location? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("episode")
    val episode: List<String?>? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("created")
    val created: String? = null
)