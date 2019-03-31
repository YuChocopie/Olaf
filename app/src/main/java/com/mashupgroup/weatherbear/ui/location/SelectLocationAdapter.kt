package com.mashupgroup.weatherbear.ui.location

import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ItemLocalListBinding
import java.util.*

class SelectLocationAdapter(private var itemEventListener: IItemChangedRequestListener) :
        RecyclerView.Adapter<SelectLocationAdapter.Companion.SelectItemVH>(),
        SelectLocationItemTouchCallback.IOnItemEditListener {
    var itemList = ArrayList<SelectLocationItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectItemVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_local_list, parent, false)
        return SelectItemVH(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SelectItemVH, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { itemEventListener.onItemClicked(position) }
    }

    fun setData(dataList : ArrayList<SelectLocationItem>) {
        itemList = dataList
        notifyDataSetChanged()
    }

    // When item's order is changed by drag
    override fun onItemMoved(posFrom: Int, posTo: Int) {
        Collections.swap(itemList, posFrom, posTo)
        notifyItemMoved(posFrom, posTo)
        itemEventListener.onRequestedItemSwap()
    }

    // When Item is swiped out
    override fun onItemSwiped(pos: Int) {
        itemEventListener.onRequestedItemRemove(itemList[pos].address)
    }

    companion object {
        class SelectItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(vmItem : SelectLocationItem) {
                // 아이템 뷰모델을 바인딩하고, 롱프레스 리스너를 연결한다
                val binding: ItemLocalListBinding = DataBindingUtil.bind(itemView!!)!!
                binding.localData = vmItem.viewModel
            }
        }

        interface IItemChangedRequestListener {
            fun onRequestedItemSwap()
            fun onRequestedItemRemove(address: Address)
            fun onItemClicked(idx: Int)
        }
    }
}