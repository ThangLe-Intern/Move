package com.madison.move.ui.offlinechannel

import android.os.Message
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface CommentContract {

    interface CommentContract : BaseView{
        fun onSuccessGetVideoSuggestion(videoDetailsSuggestionResponse: VideoDetailResponse)

        fun onError(errorMessage: String)
    }
    interface CommentPresenter : BasePresenter<CommentContract>{

        fun getVideoDetail(authorization: String,id: Int)

    }

}