package com.example.wings.network

import com.example.wings.model.CustomerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET("users")
    fun getCustomer(@Query("per_page") token: Int,@Query("since") since: Int): Call<List<CustomerResponse>>

}