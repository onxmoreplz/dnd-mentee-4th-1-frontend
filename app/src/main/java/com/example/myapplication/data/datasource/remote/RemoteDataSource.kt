package com.example.myapplication.data.datasource.remote

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.myapplication.App
import com.example.myapplication.data.datasource.remote.api.RecipeApi
import com.example.myapplication.data.datasource.remote.api.RecipeDTO
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class RemoteDataSource {

    private val recipeApi = RecipeApi.create()

    fun getAllTimelinesFromRemote(
        success: (RecipeDTO.PostItems) -> Unit,
        fail: (Throwable) -> Unit
    ) {
        val callGetAllTimelines = recipeApi.getAllTimelines()
        callGetAllTimelines.enqueue(object : retrofit2.Callback<RecipeDTO.PostItems> {
            override fun onResponse(
                call: Call<RecipeDTO.PostItems>,
                response: Response<RecipeDTO.PostItems>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        success(it)
                        val result: RecipeDTO.PostItems? = response.body()
                        Log.d(TAG, "성공 : ${response.raw()}")
                        Log.d("result", result?.get(0)?.title.toString())
                    }
                }
            }

            override fun onFailure(call: Call<RecipeDTO.PostItems>, t: Throwable) {
                Log.e("/posts", "AlltimelinesFromRemote 가져오기 실패 : " + t)
            }
        })

    }

    fun getRandomRecipes(
        success: (RecipeDTO.RecipeFinal) -> Unit,
        fail: (Throwable) -> Unit
    ) {
        val callGetRandomRecipes = recipeApi.getRandomRecipes("search", "수배")
        callGetRandomRecipes.enqueue(object : retrofit2.Callback<RecipeDTO.RecipeFinal> {
            override fun onResponse(
                call: Call<RecipeDTO.RecipeFinal>,
                response: Response<RecipeDTO.RecipeFinal>
            ) {
                response.body()?.let {
                    success(it)
                }
            }

            override fun onFailure(call: Call<RecipeDTO.RecipeFinal>, t: Throwable) {
                Log.e("/posts", "RandomRecipes 가져오기 실패 : " + t)
            }
        })
    }

//    fun postTimeline(
//        postInfo: ArrayList<RecipeDTO.PostItem>,
//        success: (RecipeDTO.TimelineResponse) -> Unit,
//        fail: (Throwable) -> Unit
//    ) {
//
//    }

    fun postImageUpload(
        imagePath: String,
        success: (RecipeDTO.UploadImage) -> Unit,
        fail: (Throwable) -> Unit
    ) {
        var file = File(imagePath)
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out)

        Log.d("fileName", file.name + " " + "여긴데1")
        Log.d("fileName", file.path + " " + "여긴데2")
        if (file.exists()) {
            Log.d("파일 존재", file.absolutePath)
        } else {
            Log.d("파일 없음", "상위 디렉토리 생성 ")
            file.mkdirs()
        }
        val requestBody: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"), out.toByteArray()
        )
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "data",
            file.name,
            requestBody
        )

        val callPostImageUpload = recipeApi.postImageUpload(body)
        callPostImageUpload.enqueue(object : Callback<RecipeDTO.UploadImage> {
            override fun onResponse(
                call: Call<RecipeDTO.UploadImage>,
                response: Response<RecipeDTO.UploadImage>
            ) {
                if (response?.isSuccessful) {
                    Toast.makeText(App.instance, "이미지 업로드 성공!", Toast.LENGTH_SHORT).show()
                    // Log.d("image upload success!!1", response?.body().toString())
                    response.body()?.let {
                        success(it)
                    }
                } else {
                    Toast.makeText(App.instance, "실패...", Toast.LENGTH_SHORT).show()
                    Log.d("image upload fail....", response.message())
                    fail
                }
            }

            override fun onFailure(call: Call<RecipeDTO.UploadImage>, t: Throwable) {
                Log.d("image upload fail!!", t.message.toString())
            }
        })
    }

    fun postRecipeUpload(
        recipeInfo : (RecipeDTO.RecipeFinal),
        success: (RecipeDTO.RecipeFinal) -> Unit,
        fail: (Throwable) -> Unit
    ) {
        var data :HashMap<String, Any> = HashMap<String, Any>()
        data.put("themeIds", recipeInfo.themes)
        data.put("thumbnail", recipeInfo.thumbnail.toString())
        data.put("starCount", recipeInfo.starCount.toString())
        data.put("mainIngredients", recipeInfo.mainIngredients)
        data.put("subIngredients", recipeInfo.subIngredients)
        data.put("id", recipeInfo.id.toString())
        data.put("time", recipeInfo.time.toString())
        data.put("viewCount", recipeInfo.viewCount.toString())
        data.put("writerId", recipeInfo.writer.toString())
        data.put("title", recipeInfo.title.toString())
        data.put("subtitle", recipeInfo.subtitle.toString())
        data.put("wishCount", recipeInfo.wishCount.toString())
        data.put("steps", recipeInfo.steps.toString())
        
        val callPostRecipeUpload = recipeApi.postRecipeUpload(data)
        callPostRecipeUpload.enqueue(object : Callback<RecipeDTO.RecipeFinal> {
            override fun onResponse(
                call: Call<RecipeDTO.RecipeFinal>,
                response: Response<RecipeDTO.RecipeFinal>
            ) {
                if (response?.isSuccessful) {
                    Toast.makeText(App.instance, "레시피 업로드 성공!", Toast.LENGTH_SHORT).show()
                    response.body()?.let {
                        success(it)
                    }
                } else {
                    Toast.makeText(App.instance, "실패...", Toast.LENGTH_SHORT).show()
                    Log.d("recipe upload fail....", response.message())
                    fail
                }
            }

            override fun onFailure(call: Call<RecipeDTO.RecipeFinal>, t: Throwable) {
                Log.d("recipe upload fail!!", t.message.toString())
            }
        })
    }
}