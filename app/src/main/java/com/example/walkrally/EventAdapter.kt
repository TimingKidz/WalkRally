package com.example.walkrally

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EventAdapter(val mCtx: Context, val resources: Int, val items: List<EventClass>)
    : ArrayAdapter<EventClass>(mCtx, resources, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources,null)

        val textViewName = view.findViewById<TextView>(R.id.TeamName)
        val textViewN = view.findViewById<TextView>(R.id.TeamN)

        var mItem:EventClass = items[position]

        textViewName.text = mItem.name
        textViewN.text = mItem.mcount.toString()

        return view
    }
}