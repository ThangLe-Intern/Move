package com.madison.move.ui.guidelines

import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.madison.move.R
import com.madison.move.data.model.DataGuidelines
import com.madison.move.data.model.ObjectResponse
import com.madison.move.databinding.FragmentGuidelineBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.menu.MainInterface

class GuidelinesFragment : BaseFragment<GuidelinePresenter>(),GuidelineContract.GuidelineView {
    private lateinit var binding: FragmentGuidelineBinding
    private lateinit var adapterGuidelines: GuidelinesAdapter
    var dataGuidelines = ArrayList<DataGuidelines>()
    private var isGetGuidelineData = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuidelineBinding.inflate(inflater,container,false)
        binding.recyclerviewCAndG.isNestedScrollingEnabled = false
        return binding.root

    }

    override fun createPresenter(): GuidelinePresenter = GuidelinePresenter(this)

    override fun onResume() {
        super.onResume()
        //Check Internet Connection
        if (mListener?.isDeviceOnlineCheck() == false) {
            mListener?.onShowDisconnectDialog()
        }else{
            mListener?.onShowProgressBar()
            presenter?.getGuidelinesData()
        }
    }

    override fun onSuccessGuidelineData(dataGuidelineResponse: ObjectResponse<List<DataGuidelines>>) {
       isGetGuidelineData = true
        dataGuidelines.addAll(dataGuidelineResponse.data ?: emptyList())
        presenter?.onShowListGuidelinePresenter(dataGuidelines)
    }

    override fun onError(errorMessage: String) {
        mListener?.onHideProgressBar()
        mListener?.onShowDisconnectDialog()
    }

    override fun onShowListGuideline(dataGuideline: ArrayList<DataGuidelines>) {
        adapterGuidelines = GuidelinesAdapter(this,dataGuideline)
        binding.recyclerviewCAndG.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterGuidelines
        }
        if (isGetGuidelineData){
            mListener?.onHideProgressBar()
        }
    }
}