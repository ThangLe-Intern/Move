package com.madison.move.ui.guidelines

import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.madison.move.R
import com.madison.move.databinding.FragmentGuidelineBinding

class GuidelinesFragment :Fragment() {
    private lateinit var binding: FragmentGuidelineBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuidelineBinding.inflate(inflater,container,false)

//
//
//            val htmlString = getString(R.string.tv2)
//
//            // Chuyển đổi HTML thành định dạng Spanned
//            val spannedText = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
//
//        // Tạo một đối tượng SpannableStringBuilder
//        val spannableBuilder = SpannableStringBuilder(spannedText)
//
//        // Thiết lập khoảng cách đầu dòng cho mỗi dấu chấm của danh sách không có thứ tự
//        val indentWidth = 10 // Khoảng cách giữa dấu chấm và văn bản (đơn vị px)
//        val bulletGapWidth = 20 // Khoảng cách giữa các dấu chấm (đơn vị px)
//        spannableBuilder.setSpan(
//            LeadingMarginSpan.Standard(indentWidth, bulletGapWidth),
//            0,
//            spannableBuilder.length,
//            0
//        )
//
//        // Đặt văn bản đã định dạng trên TextView
//        binding.tv2.text = spannableBuilder

        return binding.root

    }
}