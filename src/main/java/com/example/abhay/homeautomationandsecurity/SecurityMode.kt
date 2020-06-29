package com.example.abhay.homeautomationandsecurity

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*
import kotlinx.android.synthetic.main.activity_security_mode.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream




class SecurityMode : AppCompatActivity() {
    private var textToSpeech:TextToSpeech? = null
    private var deviceAddress: String = "HC-05"
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    private var device: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private val receive=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_mode)

        val rooms = arrayOf("Bedroom", "Dining Room", "Guest Room", "Drawing Room")

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

        @SuppressLint("HandlerLeak")
        val h = object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                if (msg.what == receive){
                        val readBuf = msg.obj as ByteArray
                        val strIncom = String(readBuf, 0, msg.arg1)
                        if (strIncom == "1"){
                            addNotification("Suspicious Activity Observed")
                        }
                }
            }
        }

        if(btInit()){
            btConnect()
            Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show()
        }

        trigger_camera.setOnClickListener{
            outputStream?.write("1".toByteArray())
            textToSpeech!!.speak("Security device is now opening", TextToSpeech.QUEUE_FLUSH, null,"")

        }
/*
        Timer().schedule(object : TimerTask() {
            override fun run() {
                // this code will be executed after 20 seconds
                addNotification("Suspicious Activity Observed")
            }
        }, 120000)
*/


        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, rooms)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@SecurityMode, "Room =" + " " + rooms[position], Toast.LENGTH_SHORT).show()
                    //textToSpeech!!.speak("Welcome to Home Security", TextToSpeech.QUEUE_FLUSH, null,"")


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        class ReceiveFromArduino: Thread() {
            override fun run() {
                var buffer = byteArrayOf(256.toByte())
                var bytes = 0

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = inputStream!!.read(buffer)
                        h.obtainMessage(receive, bytes, -1, buffer).sendToTarget()}
                    catch (e:IOException) {
                        break
                    }
                }
            }
        }
    }

    private fun addNotification(mess:String) {

        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_send) //set icon for notification
                .setContentTitle(mess) //set title of notification
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_MAX) //set priority of notification
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)



        // Add as notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
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
