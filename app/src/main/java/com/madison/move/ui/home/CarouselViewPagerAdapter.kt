package com.madison.move.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.madison.move.R

class CarouselViewPagerAdapter(var listFragment:ArrayList<FeaturedFragment>, private val viewPager2: ViewPager2):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBind(){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_featured,parent,false))
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == listFragment.size-1){
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        listFragment.addAll(listFragment)
        notifyDataSetChanged()
    }
}