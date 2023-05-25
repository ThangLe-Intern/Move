package com.madison.move.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madison.move.R
import com.madison.move.data.model.category.DataCategory
import com.madison.move.databinding.ItemCategoryBinding
import com.madison.move.ui.home.HomeFragment

class CategoryAdapter(
    var activity: HomeFragment,
    var listCategory: ArrayList<DataCategory>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: DataCategory) {

            if (category.img != null){
                Glide.with(activity)
                    .load(category.img)
                    .into(binding.imgCategory)
            }

            binding.txtCategoryType.text = category.name
//            binding.txtCategoryView.text = "${category.viewCount} views"
            binding.txtCategoryView.text = activity.getString(R.string.count_view, category.viewCount.toString() ?:"")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listCategory[position])
    }
}
