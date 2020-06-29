package com.example.abhay.homeautomationandsecurity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_access_mode.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast



class AccessMode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_mode)
        val device = intent.getStringExtra ("DEVICE")
        val db = DatabaseHandler(this)
        val noOfSwitches = arrayOf(1, 2, 3, 4)
        val rooms = arrayOf("Bedroom", "Dining Room", "Guest Room", "Drawing Room")
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        if (spinner2 != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, rooms)
            spinner2.adapter = arrayAdapter

            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@AccessMode, "Room =" + " " + rooms[position], Toast.LENGTH_SHORT).show()



                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }


        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, noOfSwitches)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //Toast.makeText(this@AccessMode, "Number of Switches =" + " " + noOfSwitches[position], Toast.LENGTH_SHORT).show()
                    val sLeft = db.allSwitch
                    if (sLeft.size != 0) {
                        for (i in 0 until sLeft.size) {
                            db.deleteSwitch(sLeft[i])
                        }
                    }

                    for (i in 0 until noOfSwitches[position]){
                        db.addSwitch(Switch("Switch"+(i+1)))
                    }

                    val s = db.allSwitch
                    val listItems = arrayOfNulls<String>(s.size)
                    for (i in 0 until s.size) {
                        val recipe = s[i]
                        listItems[i] = recipe.name
                    }
                    val adapter = SwitchList(this@AccessMode,listItems)

                    switches.adapter = null
                    //val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listItems)
                    switches.adapter = adapter

                    switches.onItemClickListener = AdapterView.OnItemClickListener { pa, v, posi, i ->

                        val intent = Intent(applicationContext, AccessSwitchPage ::class.java)
                        intent.putExtra("ID", posi.toString())
                        intent.putExtra("VALUE", pa.getItemAtPosition(posi).toString())
                        intent.putExtra("DEVICE",device)

                        startActivity(intent)


                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }




    }
    override fun onResume() {
        super.onResume()

        val db=DatabaseHandler(this)
        val s = db.allSwitch
        val listItems = arrayOfNulls<String>(s.size)
        for (i in 0 until s.size) {
            val recipe = s[i]
            listItems[i] = recipe.name
        }
        val adapter = SwitchList(this@AccessMode,listItems)
        switches.adapter = null
        //val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listItems)
        switches.adapter = adapter
    }



}

