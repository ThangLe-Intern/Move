package com.madison.move.ui.offlinechannel

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.videodetail.VideoDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentPresenter(
    override var view: CommentContract.CommentContract?

) : CommentContract.CommentPresenter {

    private val dataManager: DataManager = DataManager.instance

    override fun getVideoDetail(authorization: String, id: Int) {
        dataManager.movieRepository.getVideoDetail(authorization, id)?.enqueue(object :
            Callback<VideoDetailResponse> {
            override fun onResponse(
                call: Call<VideoDetailResponse>,
                response: Response<VideoDetailResponse>
            ) {
                if (response.body() != null) {
                    view?.onSuccessGetVideoSuggestion(response.body()!!)
                }
            }

            override fun onFailure(call: Call<VideoDetailResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                view?.onError(t.message.toString())
            }

        })
    }


}