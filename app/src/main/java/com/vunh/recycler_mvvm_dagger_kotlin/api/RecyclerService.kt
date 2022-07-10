package com.vunh.recycler_mvvm_dagger_kotlin.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vunh.recycler_mvvm_dagger_kotlin.utils.AppUtils
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.model.Response
import com.vunh.recycler_mvvm_dagger_kotlin.model.Status
import com.vunh.recycler_mvvm_dagger_kotlin.utils.Constant
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RecyclerService {

//    @FormUrlEncoded
    @GET("pda/movie/list")
    fun callListAsync(
//        @Field("username") username: String,
//        @Field("password") password: String,
    ): Deferred<List<Movie>>

    @GET("pda/movie/get")
    fun callGetAsync(
        @Query("movieId") movieId: String,
    ): Deferred<Response>

    @FormUrlEncoded
    @POST("pda/movie/add")
    fun callInsertAsync(
        @Field("movieId") movieId: String,
        @Field("category") category: String,
        @Field("imageUrl") imageUrl: String,
        @Field("name") name: String,
        @Field("desc") desc: String,
    ): Deferred<Status>

    @FormUrlEncoded
    @POST("pda/movie/update")
    fun callUpdateAsync(
        @Field("movieId") movieId: String,
        @Field("category") category: String,
        @Field("imageUrl") imageUrl: String,
        @Field("name") name: String,
        @Field("desc") desc: String,
    ): Deferred<Status>

    @FormUrlEncoded
    @POST("pda/movie/delete")
    fun callDeleteAsync(
        @Field("movieId") movieId: String,
    ): Deferred<Status>

    companion object {
        var recyclerService: RecyclerService? = null

        fun getInstance() : RecyclerService {
            if (recyclerService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()

                recyclerService = retrofit.create(RecyclerService::class.java)
            }
            return recyclerService!!
        }
    }
}