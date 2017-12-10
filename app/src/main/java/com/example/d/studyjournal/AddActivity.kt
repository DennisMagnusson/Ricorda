package com.example.d.studyjournal

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.text.method.KeyListener
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.example.d.studyjournal.R.id.textView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var addLayout: LinearLayout
    private lateinit var addButton: Button
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(findViewById(R.id.addToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addLayout = findViewById(R.id.addLayout)
        addButton = findViewById(R.id.addButton)
        editText = findViewById(R.id.editText)
        editText.setOnKeyListener(View.OnKeyListener { view, i, keyEvent -> onEnter(keyEvent, i)})

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(prefs.getBoolean("night_switch", false)) {
            findViewById<AppCompatImageButton>(R.id.doneButton).setColorFilter(Color.WHITE)
        }

        findViewById<AppCompatImageButton>(R.id.doneButton).setOnClickListener {
            writeToFile()
            startActivity(Intent(this, MainActivity::class.java))
        }

        addButton.setOnClickListener {
            if(editText.text.toString() != "")
                addLayout.addView(createTextView(editText.text.toString()))
            editText.text.clear()
        }
    }

    private fun onEnter(event: KeyEvent, i: Int): Boolean {
        if(i == KeyEvent.KEYCODE_ENTER) {
            if(editText.text.toString() != "")
                addLayout.addView(createTextView(editText.text.toString()))
            editText.text.clear()
            return true
        }

        return false
    }

    private fun writeToFile() {
        var date = Date()
        var format = SimpleDateFormat(resources.getString(R.string.date_format))
        val dateString = format.format(date)

        val filename = applicationContext.filesDir.path + "/.studyJournal"
        var fileDirectory = File(filename)

        try {
            var stream = FileOutputStream(fileDirectory, true)
            var writer = OutputStreamWriter(stream)
            var i = 0
            while(i < addLayout.childCount) {//The things
                val k = addLayout.getChildAt(i)
                if(k is LinearLayout) {
                    val textView = k.getChildAt(0) as TextView
                    Log.i("Writing to file: ", textView.text.toString())
                    writer.append(dateString + " " + textView.text.toString())
                    writer.append("\n")
                }
                i++
            }
            writer.close()
            stream.flush()
            stream.close()

        } catch(e: IOException) {
            Log.e("ERROR", "The second error thing.")
        }
    }

    private fun createTextView(text:String): LinearLayout {
        var layout = LinearLayout(this@AddActivity)
        layout.orientation = LinearLayout.HORIZONTAL

        val textView = TextView(this@AddActivity)
        textView.text = text

        val button = Button(this@AddActivity)
        button.setOnClickListener {
            addLayout.removeView(layout)
        }
        button.text = "X"

        layout.addView(textView)
        layout.addView(button)

        return layout
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return super.onSupportNavigateUp()
    }



}
