package com.example.walkrally

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaderAdapter(val items: ArrayList<Team>) : RecyclerView.Adapter<LeaderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_learderboard_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(team: Team) {
            val name = itemView.findViewById(R.id.TeamName) as TextView
            val points  = itemView.findViewById(R.id.TeamN) as TextView
            name.text = team.name
            var S = (team.Ttime.div(1000))%60
            var M = ((team.Ttime.div(1000*60))%60).toInt()

            points.text = "$M:$S"
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}