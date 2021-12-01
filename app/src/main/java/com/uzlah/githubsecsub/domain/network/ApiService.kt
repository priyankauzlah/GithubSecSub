package com.uzlah.githubsecsub.domain.network

import android.os.Build
import com.uzlah.githubsecsub.BuildConfig
import com.uzlah.githubsecsub.domain.model.DetailUserResponse
import com.uzlah.githubsecsub.domain.model.ItemsItem
import com.uzlah.githubsecsub.domain.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?q=followers%3A>%3D1000&ref=searchresults&s=followers&type=Users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getListUser():UserResponse

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getSearchUser(@Query("q") username : String) : UserResponse

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getDetailUser(@Path("username")username: String) : DetailUserResponse

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getFollowers(@Path("username")username: String) : List<ItemsItem>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getFollowing(@Path("username")username: String) : List<ItemsItem>


}