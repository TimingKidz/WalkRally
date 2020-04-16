package com.example.walkrally

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamMemberAdapter(val items: ArrayList<User>) : RecyclerView.Adapter<TeamMemberAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_teammember_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TeamMemberAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: User) {
            val name = itemView.findViewById(R.id.MemberName) as TextView
            Log.d("a00",user.name)
            name.text = user.name

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}