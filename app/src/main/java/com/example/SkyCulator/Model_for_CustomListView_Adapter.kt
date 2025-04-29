package com.example.SkyCulator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class Model_for_CustomListView_Adapter (var mCtx:Context, var resources:Int, var items:List<Model_for_CustomListView>):ArrayAdapter<Model_for_CustomListView>(mCtx, resources, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(resources, null)

        val imageView:ImageView = view.findViewById(R.id.RowImage)
        val titleTextView:TextView = view.findViewById(R.id.textView_title)
        val weight:TextView = view.findViewById(R.id.textView_show_weight)
        val equipment:TextView = view.findViewById(R.id.textView_show_equipment)
        val canopy:TextView = view.findViewById(R.id.textView_show_canopy)

        val mItem:Model_for_CustomListView = items[position]
        imageView.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        titleTextView.text = mItem.description
        weight.text =  mItem.weight
        equipment.text =  mItem.equipment
        canopy.text =  mItem.canopy
        return view
    }



}