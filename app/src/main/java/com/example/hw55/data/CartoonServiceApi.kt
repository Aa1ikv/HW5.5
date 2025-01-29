package com.example.hw55.data

import com.example.hw55.data.models.BaseResponce
import retrofit2.Call
import retrofit2.http.GET


interface CartoonApiService {

    @GET("character")
    fun getCharacters(): Call<BaseResponce>

}