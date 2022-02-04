package com.example.SkyDiver

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
        val titleTextView:TextView = view.findViewById(R.id.textView1)
        val descriptionTextView:TextView = view.findViewById(R.id.textView2)

        var mItem:Model_for_CustomListView = items[position]
        imageView.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        titleTextView.text = mItem.title
        descriptionTextView.text = mItem.description

        return view
    }



}