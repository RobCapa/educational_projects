package com.example.recyclerview.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/api/character")
    fun loadList(@Query("page") page:Int): Call<Response>

    companion object{
        const val pageSize = 20

        val retrofit by lazy {
            Retrofit
                .Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<Api>()
        }
    }
}

class Response(
    val results:List<Character>
)

class Character(
    val id:Long,
    val name:String,
    val species:String,
)