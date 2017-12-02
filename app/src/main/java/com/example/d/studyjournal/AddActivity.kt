package com.example.d.studyjournal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
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

        //this.supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_btn_check_material)
        //TODO Fix this. This doesn't work
        //actionBar.setDisplayHomeAsUpEnabled(true)

        //this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_)

        addLayout = findViewById(R.id.addLayout)
        addButton = findViewById(R.id.addButton)
        editText = findViewById(R.id.editText)

        addButton.setOnClickListener {
            try {
                addLayout.addView(createTextView(editText.text.toString()))
            } catch (e:Exception) {
                Log.i("TJKTJ", e.message)
            }
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



}
