package com.example.wings.network

import androidx.appcompat.app.AppCompatActivity
import com.example.wings.R
import com.example.wings.model.CustomerResponse
import com.example.wings.network.ApiResponseInterface
import com.example.wings.utils.Const
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommonCall(val activity: AppCompatActivity, val apiResponseInterface: ApiResponseInterface) {

    fun customer(size : Int,since : Int) {
        if (!Const.isInternetAvailable(activity)) {
            Const.showSnackBar(activity, activity.getString(R.string.no_internet))
            return
        }


        val apiInterface = ApiClient.client!!.create(ApiInterface::class.java)
        val response: Call<List<CustomerResponse>>?
        response = apiInterface.getCustomer(size,since)
        response.enqueue(object : Callback<List<CustomerResponse>> {
            override fun onResponse(
                call: Call<List<CustomerResponse>>,
                response: Response<List<CustomerResponse>>
            ) {

                try {
                    val obj = response.body()
                    if (response.isSuccessful && obj !=null) {
                        apiResponseInterface.isSuccess(obj, ApiParams.GET_CUSTOMER)
                    } else {
                        apiResponseInterface.isError("No Customer Found", ApiParams.GET_CUSTOMER)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Const.showSnackBar(activity, "Data not load proper!")
                }
            }

            override fun onFailure(call: Call<List<CustomerResponse>>, t: Throwable) {
                Const.showSnackBar(activity, "Server not responding!")
                t.printStackTrace()
            }
        })
    }

   }