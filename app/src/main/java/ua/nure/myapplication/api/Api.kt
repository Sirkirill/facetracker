package ua.nure.myapplication.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ua.nure.myapplication.api.models.FlagUpdate
import ua.nure.myapplication.api.models.Post
import ua.nure.myapplication.api.requests.LoginRequest
import ua.nure.myapplication.api.responses.DetailResponse
import ua.nure.myapplication.api.responses.LoginResponse

const val authBase = "/api/profiles"
const val postsBase = "/api/posts"

interface Api {
    @POST("$authBase/login/")
    fun login(@Body request:LoginRequest) : Call<LoginResponse>


    @GET("$postsBase/")
    fun getPosts() : Call<List<Post>>

    @GET("$authBase/logout/")
    fun logout() : Call<DetailResponse?>

    @GET("$postsBase/{id}/")
    fun getPost(@Path("id") id:Int) : Call<Post>

    @PUT("$postsBase/{id}/")
    fun updatePost(@Path("id") id:Int, @Body flags: FlagUpdate) : Call<Post>
}