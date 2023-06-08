package com.madison.move.ui.faq

import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.data.model.DataFAQ
import com.madison.move.data.model.ObjectResponse
import com.madison.move.databinding.FragmentFaqBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.menu.MainInterface


/**
 * Create by SonLe on 04/05/2023
 */
class FAQFragment : BaseFragment<FAQPresenter>(), FAQContract.FAQView {
    private lateinit var adapterFaq: FAQAdapter
    var dataFAQ = ArrayList<DataFAQ>()
    private var isGetFqaData = true

    override fun createPresenter(): FAQPresenter = FAQPresenter(this)
    private lateinit var binding: FragmentFaqBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqBinding.inflate(inflater, container, false)

        binding.tvBot.makeLinks(
            Pair("Contact us", View.OnClickListener {
                Toast.makeText(context, "Thông báo", Toast.LENGTH_SHORT).show()
            })
        )

        presenter?.getFaqData()

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        //Check Internet Connection
        if (mListener?.isDeviceOnlineCheck() == false) {
            mListener?.onShowDisconnectDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initAdapter()


    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    textPaint.color = textPaint.linkColor
                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

//    private fun initAdapter() {
//        val newList = ArrayList<FAQDataModel>()
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )
//        newList.add(
//            FAQDataModel(
//                title = getString(R.string.itemFAq),
//                plus = R.drawable.ic_plus,
//                minus = getString(R.string.itemMinus),
//            )
//        )

//        val newRecyclerView = view?.findViewById<RecyclerView>(R.id.recyclerviewFaq)
//        val adapter = FAQAdapter(tdataFAQ)
//        newRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
//        newRecyclerView?.setHasFixedSize(true) //giữ kích thước cố định cho RecyclerView
//        newRecyclerView?.adapter = adapter
//        adapter.notifyDataSetChanged()


//    }


    override fun onSuccessFaqData(dataFaqResponse: ObjectResponse<List<DataFAQ>>) {
        isGetFqaData = true

        Log.d("EE","true")

        dataFAQ.addAll(dataFaqResponse.data ?: emptyList())
        presenter?.onShowListFaqPresenter(dataFAQ)

    }

    override fun onError(errorMessage: String) {
        mListener?.onShowDisconnectDialog()

    }

    override fun onShowListFaq(dataFAQ: ArrayList<DataFAQ>) {
        adapterFaq = FAQAdapter(this, dataFAQ)
        binding.recyclerviewFaq.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterFaq
        }
        if (isGetFqaData) {
            mListener?.onHideProgressBar()
        }
    }

}


