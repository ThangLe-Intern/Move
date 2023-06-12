package com.madison.move.ui.guidelines

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.data.model.DataGuidelines
import com.madison.move.databinding.ItemScreenGuidelineBinding

class GuidelinesAdapter(
    var activity: GuidelinesFragment,
    var dataGuidelines: ArrayList<DataGuidelines>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemScreenGuidelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun getItemCount(): Int {
        return dataGuidelines.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(dataGuidelines[position])
    }

    inner class ViewHolder(val binding: ItemScreenGuidelineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(dataGuidelines: DataGuidelines?) {
            binding.apply {
                tvTitle.text = dataGuidelines?.title
                val apiHtml = dataGuidelines?.content ?: ""
                webView.loadData(apiHtml,"text/html", "UTF-8")
                webView.settings.javaScriptEnabled = true

            }

        }

    }
}