package com.mashupgroup.weatherbear.location

import android.databinding.DataBindingUtil
import android.location.Address
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ItemSearchLocationBinding
import com.mashupgroup.weatherbear.viewmodels.SearchItemViewModel

class SearchLocationAdapter(var itemClickListener: ISearchResultItemClickListener) : RecyclerView.Adapter<SearchLocationAdapter.Companion.SearchItemVH>() {
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
        holder.bind(item, itemClickListener)
    }

    fun setData(data : ArrayList<SearchItemViewModel>) {
        itemList = data
        notifyDataSetChanged()
    }

    companion object {
        class SearchItemVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(vmItem : SearchItemViewModel, clickListener : ISearchResultItemClickListener) {
                // 아이템 뷰모델을 바인딩하고, 클릭 리스너를 연결한다
                val binding: ItemSearchLocationBinding = DataBindingUtil.bind(itemView!!)!!
                binding.item = vmItem
                itemView.setOnClickListener {clickListener.onResultItemClick(vmItem.address)}
            }
        }

        interface ISearchResultItemClickListener {
            fun onResultItemClick(address : Address)
        }
    }
}