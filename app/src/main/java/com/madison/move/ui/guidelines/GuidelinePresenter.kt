package com.madison.move.ui.guidelines

import com.madison.move.data.DataManager
import com.madison.move.data.model.DataFAQ
import com.madison.move.data.model.DataGuidelines
import com.madison.move.data.model.ObjectResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuidelinePresenter(
    override var view: GuidelineContract.GuidelineView?
) : GuidelineContract.Presenter {
    private val dataManager: DataManager = DataManager.instance

    override fun getGuidelinesData() {
        dataManager.movieRepository.getGuidelines()
            ?.enqueue(object : Callback<ObjectResponse<List<DataGuidelines>>> {
                override fun onResponse(
                    call: Call<ObjectResponse<List<DataGuidelines>>>,
                    response: Response<ObjectResponse<List<DataGuidelines>>>
                ) {
                    if (response.body() != null) {
                        response.body()
                        view?.onSuccessGuidelineData(response.body()!!)
                    }
                }

                override fun onFailure(
                    call: Call<ObjectResponse<List<DataGuidelines>>>,
                    t: Throwable
                ) {
                    view?.onError(t.message ?: "")
                }
            })
    }


    override fun onShowListGuidelinePresenter(dataGuideline: ArrayList<DataGuidelines>) {
     view?.onShowListGuideline(dataGuideline)
    }
}

