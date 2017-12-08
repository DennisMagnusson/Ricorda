package com.example.d.studyjournal

import android.app.ActionBar
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RepeatFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RepeatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RepeatFragment : Fragment() {

    private var text: String? = null//The skill string

    private var mListener:OnFragmentInteractionListener? = null

    //Probably not initializing properly
    private var yesterday: LinearLayout? = null
    private var week: LinearLayout? = null
    private var month: LinearLayout?= null
    private var year: LinearLayout? = null

    private lateinit var res: android.content.res.Resources
    private var height: Int = -1

    private fun createCheckBox(text:String) : CheckBox {
        val checkBox = CheckBox(activity)
        checkBox.isChecked = false
        checkBox.text = text
        checkBox.width = ActionBar.LayoutParams.MATCH_PARENT
        checkBox.height = height

        return checkBox
    }

    private fun listener(l: LinearLayout?) {
        var i = 0
        var remove = true
        Log.i("ChildCount", l?.childCount.toString())
        while(i < l!!.childCount) {
            var a: CheckBox
            try {
                a = l?.getChildAt(i) as CheckBox
            } catch (e: ClassCastException) {
                i++
                continue
            }
            if (!a.isChecked) {
                remove = false
                break
            }
            i++
        }
        if(remove) {
            val card: CardView = l?.parent as CardView
            card.animate().translationX(l.width.toFloat())
                          .alpha(0.0f)
                          .setDuration(1000)
                          .withEndAction({card.visibility = View.GONE})
        }
    }

    private fun addCheckBox(l: LinearLayout?, text: String, ctx: Context) {
        val checkBox = createCheckBox(text)
        checkBox.setOnCheckedChangeListener { checkBox, isChecked ->  listener(l)}

        l?.addView(checkBox)
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            text = getArguments().getString("text")
        }
        Log.i("This is in", "onCreate")
    }

    override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
                                     savedInstanceState:Bundle?):View? {
        res = resources
        height = res.getDimension(R.dimen.repeat_checkbox_height).toInt()

        val view: View? = inflater!!.inflate(R.layout.fragment_repeat, container, false)

        yesterday = view?.findViewById(R.id.yesterdayLayout)
        week = view?.findViewById(R.id.weekLayout)
        month = view?.findViewById(R.id.monthLayout)
        year = view?.findViewById(R.id.yearLayout)


        val filename = context.filesDir.path + "/.studyJournal"
        Log.i("OOOOOAOOOAOAOOAOAO", filename)
        if(!File(filename).exists()) {
            FileOutputStream(filename).close()
        }

        var currentDate = Date()
        var format = SimpleDateFormat(resources.getString(R.string.date_format))
        format.parse("2017-12-08")
        val formatLength = resources.getString(R.string.date_format).length
        val str = format.format(currentDate)
        Log.i("STSTSSTSTSTSTSTDATE", str)
        Log.i("CurrentDate:", currentDate.toString())

        val reader = FileReader(filename)
        Log.i("ALSDKFJASDF", reader.readText())
        Log.i("RERERERERE", "ABC")
        for(line in reader.readLines()) {
            var str = line
            val dateStr = str.substring(0, formatLength)
            val lineDate = format.parse(dateStr)
            str = str.substring(formatLength)

            Log.i(dateStr, str)
        }


        addCheckBox(yesterday, "EEEEEEEEEE", context)
        addCheckBox(yesterday, "ABCKDK", context)
        addCheckBox(week, "ABCKDK", context)
        addCheckBox(week, "ABCKDK", context)
        addCheckBox(year, "LLLLLLLLLLLLLLLLLLLLLLL", context)
        addCheckBox(month, "OOOOOOWWWWWWWWWEEEEEOOOOOOOO", context)
        Log.i("This is in", "onCreateView")

        return view
    }

    fun onButtonPressed(uri:Uri) {
        if (mListener != null) mListener!!.onFragmentInteraction(uri)
    }

    override fun onAttach(context:Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) mListener = context as OnFragmentInteractionListener?
        else throw RuntimeException((context!!.toString() + " must implement OnFragmentInteractionListener"))
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri:Uri)
    }

    companion object {
        private val ARG_PARAM1 = "PLACEHOLDER TEXT ASLFJASLDFIEIEIEIEIEIEI"

        fun newInstance(text:String):RepeatFragment {
            val fragment = RepeatFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, text)
            fragment.setArguments(args)

            return fragment
        }

    }
}
