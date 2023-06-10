package com.madison.move.ui.offlinechannel

import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.DataComment
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface CommentContract {

    interface CommentContract : BaseView{
        fun onSuccessGetVideoSuggestion(videoDetailsSuggestionResponse: VideoDetailResponse)
        fun onSuccessGetVideoDetail(objectResponse: ObjectResponse<DataVideoDetail>)
        fun onSuccessGetCommentVideo(objectResponse: ObjectResponse<List<DataComment>>)
        fun onError(errorMessage: String)
    }
    interface CommentPresenter : BasePresenter<CommentContract>{

        fun getVideoDetail(id: Int)
        fun getCommentVideo(id: Int)

    }

}