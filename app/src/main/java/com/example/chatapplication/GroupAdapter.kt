package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter (val context: Context, val groupList: ArrayList<Group>) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.groups_layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val currentGroup = groupList[position]
        holder.textName.text = currentGroup.groupName
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_group_name)
    }
}