package com.example.d.studyjournal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

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

        addButton.setOnClickListener {
            addLayout.addView(createTextView(editText.text.toString()))
            editText.text.clear()
        }
    }

    private fun createTextView(text:String): LinearLayout {
        var layout = LinearLayout(this@AddActivity)
        layout.orientation = LinearLayout.HORIZONTAL
        //layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //layout.layoutParams.width  = ViewGroup.LayoutParams.MATCH_PARENT

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
