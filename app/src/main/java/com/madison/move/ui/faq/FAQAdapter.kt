package com.madison.move.ui.faq

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R

class FAQAdapter(private val newList: ArrayList<FAQDataModel>) :
    RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

    private val hiddenFlags = BooleanArray(newList.size) { false }
    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val FAQDataModel = newList[position]
        holder.imgPlus.setImageResource(FAQDataModel.plus)
        holder.tvMinus.text = FAQDataModel.minus
        holder.tvTitle.text = FAQDataModel.title

        if (hiddenFlags[position] && selectedPositions.contains(position)) {
            holder.tvTitle.setTextColor(holder.blueColor) // Đổi màu của TextView thành màu đỏ
            holder.imgPlus.setImageResource(R.drawable.ic_minus) // Đổi hình ảnh của ImageView thành ảnh đã chọn
            holder.linearLayout.visibility = View.VISIBLE
        } else {
            holder.linearLayout.visibility = View.GONE
            holder.tvTitle.setTextColor(Color.BLACK)
            holder.imgPlus.setImageResource(R.drawable.ic_plus)
        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearlayoutMinus)
        val imgPlus: ImageView = itemView.findViewById(R.id.imgPlus)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvMinus: TextView = itemView.findViewById(R.id.tvMinus)
        val blueColor = Color.parseColor("#2BD5BC")

        init {
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