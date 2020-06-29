package com.example.abhay.homeautomationandsecurity


import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.ViewGroup
import android.app.Activity
import android.view.View


class SwitchList(private val context: Activity, private val mainTitle: Array<String?>)// TODO Auto-generated constructor stub
    : ArrayAdapter<String>(context, R.layout.activity_switch_list, mainTitle) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.activity_switch_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView

        titleText.text = mainTitle[position]

        return rowView

    }
}


