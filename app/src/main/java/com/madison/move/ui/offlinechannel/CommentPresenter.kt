package com.madison.move.ui.offlinechannel

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.DiskLikeResponse
import com.madison.move.data.model.LikeResponse
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.PostViewResponse
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.comment.SendComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentPresenter(
    override var view: CommentContract.CommentContract?

) : CommentContract.CommentPresenter {

    private val dataManager: DataManager = DataManager.instance

    override fun getVideoDetail(id: Int) {
        dataManager.movieRepository.getVideoDetail(id)
            ?.enqueue(object : Callback<ObjectResponse<DataVideoDetail>> {
                override fun onResponse(
                    call: Call<ObjectResponse<DataVideoDetail>>,
                    response: Response<ObjectResponse<DataVideoDetail>>
                ) {
                    if (response.body() != null) {
                        view?.onSuccessGetVideoDetail(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<DataVideoDetail>>, t: Throwable) {
                    view?.onError(t.message ?: "")
                }

            })
    }

    override fun getCommentVideo(token: String, id: Int) {
        dataManager.movieRepository.getCommentVideo(token, id)
            ?.enqueue(object : Callback<ObjectResponse<List<DataComment>>> {
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

                override fun onFailure(
                    call: Call<ObjectResponse<List<DataComment>>>,
                    t: Throwable
                ) {
                    view?.onError(t.message ?: "")
                }

            })

    }

    override fun sendCommentVideo(token: String, idVideo: Int, content: SendComment) {
        dataManager.movieRepository.sendComment(token, idVideo, content)
            ?.enqueue(object : Callback<ObjectResponse<CommentResponse>> {
                override fun onResponse(
                    call: Call<ObjectResponse<CommentResponse>>,
                    response: Response<ObjectResponse<CommentResponse>>
                ) {
                    if (response.body() != null) {
                        view?.onSuccessSendCommentVideo(response.body()!!)
                    }
                    if (response.errorBody() != null) {
                        view?.onError(response.message())
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<CommentResponse>>, t: Throwable) {
                    view?.onError(t.message ?: "")
                }

            })
    }

    override fun sendReplyComment(token: String, idComment: Int, content: SendComment) {
        dataManager.movieRepository.sendReply(token, idComment, content)
            ?.enqueue(object : Callback<ObjectResponse<CommentResponse>> {
                override fun onResponse(
                    call: Call<ObjectResponse<CommentResponse>>,
                    response: Response<ObjectResponse<CommentResponse>>
                ) {
                    if (response.body() != null) {
                        view?.onSuccessSendReplyComment(response.body()!!)
                    }
                    if (response.errorBody() != null) {
                        view?.onError(response.message())
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<CommentResponse>>, t: Throwable) {
                    view?.onError(t.message ?: "")
                }

            })
    }

    override fun postView(token: String, idVideo: Int) {
        dataManager.movieRepository.postView(token, idVideo)
            ?.enqueue(object : Callback<ObjectResponse<PostViewResponse>> {
                override fun onResponse(
                    call: Call<ObjectResponse<PostViewResponse>>,
                    response: Response<ObjectResponse<PostViewResponse>>
                ) {
                    if (response.body() != null) {
                        view?.onSuccessPostView(response.body()!!)
                    }
                    if (response.errorBody() != null) {
                        view?.onError(response.message())
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<PostViewResponse>>, t: Throwable) {
                    view?.onError(t.message ?: "")
                }

            })
    }

    override fun callLikeComment(token: String, idComment: Int) {
        dataManager.movieRepository.callLikeComment(token, idComment)?.enqueue(object : Callback<LikeResponse>{
            override fun onResponse(
                call: Call<LikeResponse>,
                response: Response<LikeResponse>
            ) {
                if (response.body() != null) {
                    view?.onSuccessCallLikeComment(response.body()!!)
                }
                if (response.errorBody() != null) {
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                view?.onError(t.message ?: "")
            }

        })
    }

    override fun callDiskLikeComment(token: String, idComment: Int) {
        dataManager.movieRepository.callDiskLikeComment(token, idComment)?.enqueue(object : Callback<DiskLikeResponse>{
            override fun onResponse(
                call: Call<DiskLikeResponse>,
                response: Response<DiskLikeResponse>
            ) {
                if (response.body() != null) {
                    view?.onSuccessCallDiskLikeComment(response.body()!!)
                }
                if (response.errorBody() != null) {
                }
            }

            override fun onFailure(call: Call<DiskLikeResponse>, t: Throwable) {
                view?.onError(t.message ?: "")
            }

        })
    }


}