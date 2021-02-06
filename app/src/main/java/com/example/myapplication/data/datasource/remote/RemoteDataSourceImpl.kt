package com.example.myapplication.data.datasource.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myapplication.data.datasource.remote.api.RecipeApi
import com.example.myapplication.data.datasource.remote.api.RecipeDTO
import retrofit2.Call
import retrofit2.Response

class RemoteDataSourceImpl : RemoteDataSource {

    private val recipeApi = RecipeApi.create()

    override fun getAllTimelinesFromRemote(
        success: (RecipeDTO.PostItems) -> Unit,
        fail: (Throwable) -> Unit
    ) {
        val callGetAllTimelines = recipeApi.getAllTimelines()
        callGetAllTimelines.enqueue(object : retrofit2.Callback<RecipeDTO.PostItems>{
            override fun onResponse(
                call: Call<RecipeDTO.PostItems>,
                response: Response<RecipeDTO.PostItems>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        success(it)
                        val result : RecipeDTO.PostItems? = response.body()
                        Log.d(TAG, "성공 : ${response.raw()}")
                        Log.d("result", result?.get(0)?.title.toString())
                    }
                }
            }
            override fun onFailure(call: Call<RecipeDTO.PostItems>, t: Throwable) {
                Log.d("/posts", "getAlltimelinesFromRemote 실패 : " + t)
            }
        })

    }

    override fun postTimeline(
        postInfo: ArrayList<RecipeDTO.PostItem>,
        success: (RecipeDTO.TimelineResponse) -> Unit,
        fail: (Throwable) -> Unit
    ) {

    }
}