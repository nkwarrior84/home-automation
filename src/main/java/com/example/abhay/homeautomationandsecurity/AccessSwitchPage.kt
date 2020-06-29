package com.example.abhay.homeautomationandsecurity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_access_switch_page.*
import android.content.Intent
import android.graphics.Paint
import android.widget.Toast
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.speech.tts.TextToSpeech
import android.util.Log


class AccessSwitchPage : AppCompatActivity() {
    private var textToSpeech: TextToSpeech? = null
    private var deviceAddress: String = "HC-05"
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    private var device: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_switch_page)
        val id = intent.getStringExtra("ID").toInt()
        val name = intent.getStringExtra("VALUE")
        textView4.paintFlags = textView4.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textView4.text = "$name"
        editText.setText(name)
        Toast.makeText(this,deviceAddress,Toast.LENGTH_SHORT).show()

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(Locale.US)

                if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")
            } else {
                Toast.makeText(applicationContext, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        })

        if(btInit()){
            btConnect()
            Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            val x=editText.text.toString()
            if (x!=""){

                val db = DatabaseHandler(this)
                db.updateSwitch(Switch(id+1,x))
                finish()
            }
        }
        on.setOnClickListener {


                    Toast.makeText(this,"ON",Toast.LENGTH_SHORT).show()
                    outputStream?.write("$id:1".toByteArray())
                    textToSpeech!!.speak("The $name is now opening", TextToSpeech.QUEUE_FLUSH, null,"")


        }
        off.setOnClickListener {


                    Toast.makeText(this,"OFF",Toast.LENGTH_SHORT).show()
                    outputStream?.write("$id:0".toByteArray())
            textToSpeech!!.speak("The $name is now closing", TextToSpeech.QUEUE_FLUSH, null,"")


        }
        timer_on.setOnClickListener {
            var t = timer_edit.text.toString()
            var z=t
            t += "1"
            outputStream?.write("$id:$t".toByteArray())
            textToSpeech!!.speak("The $name will be open after $z minutes", TextToSpeech.QUEUE_FLUSH, null,"")
        }
        timer_off.setOnClickListener {
            var t = timer_edit.text.toString()
            var z=t
            t += "0"
            outputStream?.write("$id:$t".toByteArray())
            textToSpeech!!.speak("The $name will be close after $z minutes", TextToSpeech.QUEUE_FLUSH, null,"")
        }
    }
    fun btInit(): Boolean {
        var found = false

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null)
        //Checks if the device supports bluetooth
        {
            Toast.makeText(applicationContext, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
        }

        if (!bluetoothAdapter!!.isEnabled)
        //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            val enableAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableAdapter, 0)



        }

        val bondedDevices = bluetoothAdapter.bondedDevices

        if (bondedDevices.isEmpty())
        //Checks for paired bluetooth devices
        {
            Toast.makeText(applicationContext, "Please pair the device first", Toast.LENGTH_SHORT).show()
        } else {
            for (iterator in bondedDevices) {
                if (iterator.name.toString() == deviceAddress) {
                    device = iterator
                    found = true
                    break
                }
            }
        }

        return found
    }

    fun btConnect(): Boolean {
        var connected = true

        try {
            socket = device?.createRfcommSocketToServiceRecord(uuid) //Creates a socket to handle the outgoing connection
            socket?.connect()

            Toast.makeText(applicationContext,
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            connected = false
        }

        if (connected) {
            try {
                outputStream = socket?.outputStream //gets the output stream of the socket
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return connected
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }


}
