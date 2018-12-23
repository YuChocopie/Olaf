package com.mashupgroup.weatherbear.location

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ItemLocalListBinding
import com.mashupgroup.weatherbear.viewmodels.LocalViewModel

class SelectLocationAdapter(var itemLongPressListener: ISelectLocationItemListener) : RecyclerView.Adapter<SelectLocationAdapter.Companion.SelectItemVH>() {
    var itemList = ArrayList<LocalViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectItemVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_local_list, parent, false)
        return SelectItemVH(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SelectItemVH, position: Int) {
        val item = itemList[position]
        holder.bind(item, itemLongPressListener)
    }

    fun setData(dataList : ArrayList<LocalViewModel>) {
        itemList = dataList
        notifyDataSetChanged()
    }

    companion object {
        class SelectItemVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bind(vmItem : LocalViewModel, clickListener : ISelectLocationItemListener) {
                // 아이템 뷰모델을 바인딩하고, 롱프레스 리스너를 연결한다
                val binding: ItemLocalListBinding = DataBindingUtil.bind(itemView!!)!!
                binding.localData = vmItem
                itemView.setOnLongClickListener {
                    clickListener.onLongPressItem()
                    true
                }
            }
        }

        interface ISelectLocationItemListener {
            fun onLongPressItem()
        }
    }

}