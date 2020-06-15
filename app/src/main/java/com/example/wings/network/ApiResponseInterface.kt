package com.example.wings.network

interface ApiResponseInterface
{
    fun isError(errorCode: String,ServiceCode: Int)
    fun isSuccess(response: Any, ServiceCode: Int)
}