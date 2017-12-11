package tech.dennismagnusson.studyjournal

import android.app.ActionBar
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import tech.dennismagnusson.studyjournal.R
import java.io.*
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

    private var mListener: OnFragmentInteractionListener? = null

    private var yesterday: LinearLayout? = null
    private var week: LinearLayout? = null
    private var month: LinearLayout?= null
    private var year: LinearLayout? = null
    private var baseLayout: LinearLayout? = null

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
                          .withEndAction({removeCardAction(l)})
        }
    }

    private fun removeCardAction(layout: LinearLayout) {
        layout.visibility = View.GONE
        addTextIfDone()
        writeFileIfDone()
    }

    private fun addCheckBox(l: LinearLayout?, text: String) {
        if(text == "") return

        val checkBox = createCheckBox(text)
        checkBox.setOnCheckedChangeListener { checkBox, _ ->  listener(l)}

        l?.addView(checkBox)
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            text = getArguments().getString("text")
        }
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

        baseLayout = view?.findViewById(R.id.repeatBaseLayout)

        if(!isDoneFile())
            addToCards()

        if(yesterday?.childCount == 1) yesterday?.visibility = View.GONE
        if(week?.childCount == 1) week?.visibility = View.GONE
        if(month?.childCount == 1) month?.visibility = View.GONE
        if(year?.childCount == 1) year?.visibility = View.GONE

        if(isDone())
            addTextIfDone()

        return view
    }

    private fun isDoneFile(): Boolean {
        val filename = context.filesDir.path + "/.done"

        val file = File(filename)
        if(!file.exists())
            FileOutputStream(filename).close()

        val reader = FileReader(filename)

        val format = SimpleDateFormat(resources.getString(R.string.date_format))
        val dateStr = format.format(Date())
        for(line in reader.readLines()) {
            if(line == dateStr)
                return true
        }

        return false
    }

    private fun isDone(): Boolean {
        return (yesterday?.visibility == View.GONE && week?.visibility == View.GONE
            && month?.visibility == View.GONE && year?.visibility == View.GONE)
    }

    private fun addTextIfDone() {
        if(isDone()) {
            var textView = TextView(activity)
            textView.text = getString(R.string.done_text)
            baseLayout?.addView(textView)
        }
    }

    private fun writeFileIfDone() {
        if(!isDone()) return

        val filename = context.filesDir.path + "/.done"
        val file = File(filename)
        if(!file.exists())
            FileOutputStream(filename).close()

        val format = SimpleDateFormat(resources.getString(R.string.date_format))
        val dateStr = format.format(Date())
        try {
            val stream = FileOutputStream(file, false)
            val writer = OutputStreamWriter(stream)

            writer.write(dateStr)

            writer.close()
            stream.flush()
            stream.close()
        } catch(e: IOException) {
            Log.e("ERROR", "Error writing to file.")
        }
    }

    private fun addToCards() {
        val filename = context.filesDir.path + "/.studyJournal"
        //Create file if doesn't exist
        if(!File(filename).exists()) {
            FileOutputStream(filename).close()
            return
        }

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

        fun newInstance(text:String): RepeatFragment {
            val fragment = RepeatFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, text)
            fragment.setArguments(args)

            return fragment
        }

    }
}
