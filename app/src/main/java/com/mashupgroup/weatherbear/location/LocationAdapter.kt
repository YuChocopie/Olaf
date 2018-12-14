package com.mashupgroup.weatherbear.location

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class LocationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemList : ArrayList<LocationListItemViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}