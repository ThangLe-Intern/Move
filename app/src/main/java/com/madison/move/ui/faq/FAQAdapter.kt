package com.madison.move.ui.faq

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.data.model.DataFAQ
import com.madison.move.databinding.ItemFaqBinding

class FAQAdapter(
    var activity: FAQFragment,
    var dataFAQ: ArrayList<DataFAQ>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val hiddenFlags = BooleanArray(dataFAQ.size) { false }
    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataFAQ.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(dataFAQ[position])
    }

    inner class ViewHolder(val binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(dataFaq: DataFAQ?) {
            binding.apply {
                tvMinus.text = dataFaq?.content
                tvTitle.text = dataFaq?.title
            }
            val blueColor = Color.parseColor("#2BD5BC")
            binding.apply {
                if (hiddenFlags[position] && selectedPositions.contains(position)) {
                    tvTitle.setTextColor(blueColor) // Đổi màu của TextView thành màu đỏ
                    imgPlus.setImageResource(R.drawable.ic_minus) // Đổi hình ảnh của ImageView thành ảnh đã chọn
                    linearlayoutMinus.visibility = View.VISIBLE
                } else {
                    linearlayoutMinus.visibility = View.GONE
                    tvTitle.setTextColor(Color.BLACK)
                    imgPlus.setImageResource(R.drawable.ic_plus)
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                hiddenFlags[position] = !hiddenFlags[position]
                notifyDataSetChanged()
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position)
                } else {
                    selectedPositions.add(position)
                }
                notifyDataSetChanged()
            }

        }

    }
}