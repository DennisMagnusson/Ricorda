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
import kotlinx.android.synthetic.main.fragment_calendar.*
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

    private val DAYS_IN_WEEK  = 7
    private val DAYS_IN_MONTH = 30
    private val DAYS_IN_YEAR  = 365

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

    private fun addCheckBox(l: LinearLayout?, text: String) {
        val checkBox = createCheckBox(text)
        checkBox.setOnCheckedChangeListener { checkBox, _ ->  listener(l)}

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

        addToCards()

        //TODO FIXME Remove for production
        addCheckBox(yesterday, "EEEEEEEEEE")
        addCheckBox(yesterday, "ABCKDK")
        addCheckBox(week, "ABCKDK")
        addCheckBox(year, "LLLLLLLLLLLLLLLLLLLLLLL")
        addCheckBox(month, "OOOOOOWWWWWWWWWEEEEEOOOOOOOO")

        return view
    }

    private fun addToCards() {
        val filename = context.filesDir.path + "/.studyJournal"
        //Create file if doesn't exist
        if(!File(filename).exists()) FileOutputStream(filename).close()

        var format = SimpleDateFormat(resources.getString(R.string.date_format))
        val formatLength = resources.getString(R.string.date_format).length

        var currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        //Calendars needed
        val yesterdayCal = calendar.clone() as Calendar
        yesterdayCal.add(Calendar.DAY_OF_YEAR, -1)
        val weekAgoCal = calendar.clone() as Calendar
        weekAgoCal.add(Calendar.DAY_OF_YEAR, -DAYS_IN_WEEK)
        val monthAgoCal = calendar.clone() as Calendar
        monthAgoCal.add(Calendar.DAY_OF_YEAR, -DAYS_IN_MONTH)
        val yearAgoCal = calendar.clone() as Calendar
        yearAgoCal.add(Calendar.DAY_OF_YEAR, -DAYS_IN_YEAR)

        val reader = FileReader(filename)
        for(line in reader.readLines()) {
            var str = line
            val dateStr = str.substring(0, formatLength)
            str = str.substring(formatLength+1)

            val lineDate = format.parse(dateStr)
            var lineCal = Calendar.getInstance()
            lineCal.time = lineDate

            if(equalsDay(lineCal, yesterdayCal))     addCheckBox(yesterday, str)
            else if(equalsDay(lineCal, weekAgoCal))  addCheckBox(week, str)
            else if(equalsDay(lineCal, monthAgoCal)) addCheckBox(month, str)
            else if(equalsDay(lineCal, yearAgoCal))  addCheckBox(year, str)
        }
    }

    //Checks if day, month and year are equal
    private fun equalsDay(cal1: Calendar, cal2: Calendar): Boolean {
        var b = cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
        b = b && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
        b = b && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        return b
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
