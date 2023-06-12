package com.madison.move.ui.faq

import com.madison.move.data.DataManager
import com.madison.move.data.model.DataFAQ
import com.madison.move.data.model.ObjectResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQPresenter(
    override var view: FAQContract.FAQView?
) : FAQContract.Presenter {
    private val dataManager: DataManager = DataManager.instance

    override fun getFaqData() {
        dataManager.movieRepository.getFaq()
            ?.enqueue(object : Callback<ObjectResponse<List<DataFAQ>>> {
                override fun onResponse(
                    call: Call<ObjectResponse<List<DataFAQ>>>,
                    response: Response<ObjectResponse<List<DataFAQ>>>
                ) {
                    if (response.body() != null) {
                        response.body()
                        view?.onSuccessFaqData(response.body()!!)
                    }
                }

                override fun onFailure(
                    call: Call<ObjectResponse<List<DataFAQ>>>,
                    t: Throwable
                ) {
                    view?.onError(t.message ?: "")
                }
            })
    }

    override fun onShowListFaqPresenter(dataFAQ: ArrayList<DataFAQ>) {
        view?.onShowListFaq(dataFAQ)
    }

}