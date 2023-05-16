package com.madison.move.ui.faq

import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.FragmentFaqBinding


/**
 * Create by SonLe on 04/05/2023
 */
class FAQFragment : Fragment() {

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
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

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
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun initAdapter() {
        val newList = ArrayList<FAQDataModel>()
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )
        newList.add(
            FAQDataModel(
                title = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam",
                plus = R.drawable.ic_plus,
                minus = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic.",
            )
        )

        val newRecyclerView = view?.findViewById<RecyclerView>(R.id.recyclerviewFaq)
        val adapter = FAQAdapter(newList)
        newRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView?.setHasFixedSize(true) //giữ kích thước cố định cho RecyclerView
        newRecyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()


    }

}


