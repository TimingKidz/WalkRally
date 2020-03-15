package com.example.walkrally

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TeamAdapter(val mCtx: Context,val layoutResId: Int,val team_list: List<Team>)
    : ArrayAdapter<Team>(mCtx, layoutResId,team_list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val textViewName = view.findViewById<TextView>(R.id.textView)
        val team = team_list[position]

        textViewName.text = team.name

        return view
    }
}