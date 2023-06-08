package com.madison.move.ui.faq

import com.madison.move.data.model.DataFAQ
import com.madison.move.data.model.ObjectResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface FAQContract {

    interface FAQView : BaseView{
        fun onSuccessFaqData(dataFaqResponse: ObjectResponse<List<DataFAQ>>)
        fun onError(errorMessage: String)
        fun onShowListFaq(dataFAQ: ArrayList<DataFAQ>)
    }
    interface Presenter : BasePresenter<FAQView>{
        fun getFaqData()
        fun onShowListFaqPresenter(dataFAQ: ArrayList<DataFAQ>)
    }


}