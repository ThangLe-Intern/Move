package com.madison.move.data.source.remote

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.Video
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.DataCountry
import com.madison.move.data.model.DataState
import com.madison.move.data.model.ProfileRequest
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.remote.model.MoveResponse
import com.madison.move.data.source.remote.services.MoveApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoveRemoteDataSource private constructor(private val moveApi: MoveApi) : MoveDataSource {
    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        moveApi.getMovies()?.enqueue(object : Callback<MoveResponse?> {
            override fun onResponse(
                call: Call<MoveResponse?>,
                response: Response<MoveResponse?>
            ) {
                val movies: List<Video?>? =
                    if (response.body() != null) response.body()!!.videos else null
                if (movies != null && movies.isNotEmpty()) {
                    callback?.onVideosLoaded(movies)
                } else {
                    callback?.onDataNotAvailable()
                }
            }

            override fun onFailure(call: Call<MoveResponse?>, t: Throwable) {
                callback?.onError()
            }
        })
    }


    override fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>> {
        return moveApi.getCarousel()
    }


    override fun getCategory(): Call<ObjectResponse<List<DataCategory>>> {
        return moveApi.getCategory()
    }

    override fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>> {
        return moveApi.getVideoSuggestion()
    }

    override fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>> {
        return moveApi.getVideoSuggestionForUser(token)
    }

    override fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>> {
        return moveApi.loginApi(email, password)
    }

    override fun logOutUser(token: String): Call<ObjectResponse<DataUser>> {
        return moveApi.logoutRequest(token)
    }

    override fun getUserProfile(token: String): Call<ObjectResponse<DataUser>> {
        return moveApi.getUserProfile(token)
    }

    override fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>? {
        return moveApi.getCountry()
    }

    override fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>> {
        return moveApi.getStates(countryID)
    }


    override fun updateProfileUser(
        token: String,
        profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>? {
        return moveApi.updateProfile(token, profileRequest)
    }

    override fun saveVideos(videos: List<Video?>?) {

    }

    companion object {
        private var instance: MoveRemoteDataSource? = null
        fun getInstance(movieApi: MoveApi): MoveRemoteDataSource {
            if (instance == null) {
                instance = MoveRemoteDataSource(movieApi)
            }
            return instance!!
        }
    }
}