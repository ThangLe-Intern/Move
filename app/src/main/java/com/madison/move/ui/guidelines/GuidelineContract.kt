package com.madison.move.ui.guidelines

import com.madison.move.data.model.DataFAQ
import com.madison.move.data.model.DataGuidelines
import com.madison.move.data.model.ObjectResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface GuidelineContract {
    interface GuidelineView : BaseView {
        fun onSuccessGuidelineData(dataGuidelineResponse: ObjectResponse<List<DataGuidelines>>)
        fun onError(errorMessage: String)
        fun onShowListGuideline(dataGuideline: ArrayList<DataGuidelines>)
    }
    interface Presenter : BasePresenter<GuidelineView> {
        fun getGuidelinesData()
        fun onShowListGuidelinePresenter(dataGuideline: ArrayList<DataGuidelines>)
    }

}