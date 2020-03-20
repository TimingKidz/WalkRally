package com.example.walkrally

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class LeaderAdapter(val mCtx: Context, val resources: Int, val items: List<Team>)
    : ArrayAdapter<Team>(mCtx, resources, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources,null)

        val textViewName = view.findViewById<TextView>(R.id.TeamName)
        val textViewN = view.findViewById<TextView>(R.id.TeamN)

        var mItem:Team = items[position]
        Log.d("OB", mItem.name)
        Log.d("OB", mItem.mcount)
        textViewName.text = mItem.name
        textViewN.text = mItem.score.toString()

        return view
    }
}