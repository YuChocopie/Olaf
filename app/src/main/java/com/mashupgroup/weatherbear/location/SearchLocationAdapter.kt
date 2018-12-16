package com.mashupgroup.weatherbear.location

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ItemSearchLocationBinding

class SearchLocationAdapter : RecyclerView.Adapter<SearchLocationAdapter.Companion.SearchItemVH>() {
    var itemList = ArrayList<SearchItemViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search_location, parent, false)
        return SearchItemVH(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SearchItemVH, position: Int) {
        val item = itemList[position]
        holder.binding.item = item
    }

    fun setData(data : ArrayList<SearchItemViewModel>) {
        itemList = data
        notifyDataSetChanged()
    }

    companion object {
        class SearchItemVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var binding: ItemSearchLocationBinding = DataBindingUtil.bind(itemView!!)!!
        }
    }
}