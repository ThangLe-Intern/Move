package com.madison.move.ui.offlinechannel

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.DataComment
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentPresenter(
    override var view: CommentContract.CommentContract?

) : CommentContract.CommentPresenter {

    private val dataManager: DataManager = DataManager.instance

    override fun getVideoDetail(id: Int) {
        dataManager.movieRepository.getVideoDetail(id)?.enqueue(object :
            Callback<ObjectResponse<DataVideoDetail>> {
            override fun onResponse(
                call: Call<ObjectResponse<DataVideoDetail>>,
                response: Response<ObjectResponse<DataVideoDetail>>
            ) {
                if (response.body() != null) {
                    view?.onSuccessGetVideoDetail(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ObjectResponse<DataVideoDetail>>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                view?.onError(t.message.toString())
            }

        })
    }

    override fun getCommentVideo(id: Int) {

        dataManager.movieRepository.getCommentVideo(id)?.enqueue(object :
            Callback<ObjectResponse<List<DataComment>>> {
            override fun onResponse(
                call: Call<ObjectResponse<List<DataComment>>>,
                response: Response<ObjectResponse<List<DataComment>>>
            ) {
                if (response.body() != null) {
                    view?.onSuccessGetCommentVideo(response.body()!!)
                }
                if (response.errorBody() != null) {
                }
            }

            override fun onFailure(call: Call<ObjectResponse<List<DataComment>>>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                view?.onError(t.message.toString())
            }

        })

    }


}