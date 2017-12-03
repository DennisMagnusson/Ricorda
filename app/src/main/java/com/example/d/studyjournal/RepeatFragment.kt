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
import android.widget.LinearLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

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
    var yesterday: LinearLayout? = null
    var week: LinearLayout? = null
    var month: LinearLayout?= null
    var year: LinearLayout? = null

    private lateinit var res: android.content.res.Resources
    //TODO Make private
    private var height: Int = -1

    //TODO  Make private
    private fun createCheckBox(text:String) : CheckBox {
        val checkBox = CheckBox(activity)
        checkBox.isChecked = false
        checkBox.text = text
        checkBox.width = ActionBar.LayoutParams.MATCH_PARENT
        checkBox.height = height
        return checkBox
    }

    private fun addCheckBox(l: LinearLayout?, text: String, ctx: Context) {
        val checkBox = createCheckBox(text)
        l?.addView(checkBox)
    }

    public override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            text = getArguments().getString("text")
        }
        Log.i("This is in", "onCreate")
    }

    public override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
                                     savedInstanceState:Bundle?):View? {
        // Inflate the layout for this fragment
        //WOOOHOOO This works
        res = resources
        height = res.getDimension(R.dimen.repeat_checkbox_height).toInt()

        //val v: View? = view?.findViewById(R.id.repeatTab)
        val view: View? = inflater!!.inflate(R.layout.fragment_repeat, container, false)

        yesterday = view?.findViewById(R.id.yesterdayLayout)
        layoutOnClickListener(yesterday)
        /*
        yesterday?.setOnClickListener {
            var c: Int = yesterday!!.childCount
            var i: Int = 0
            while(i < this.childCount) {
                this.getChildAt()
                i++
            }
        }
        year.setOnTouchListener()
        */
        week = view?.findViewById(R.id.weekLayout)
        layoutOnClickListener(week)
        month = view?.findViewById(R.id.monthLayout)
        layoutOnClickListener(month)
        year = view?.findViewById(R.id.yearLayout)
        layoutOnClickListener(year)

        yesterday?.addView(createCheckBox("EEEEEEEEEEEEEEEEEEEEEEEEEEE"))
        addCheckBox(yesterday, "ABCKDK", context)
        addCheckBox(week, "ABCKDK", context)
        addCheckBox(week, "ABCKDK", context)
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

    public override fun onAttach(context:Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) mListener = context as OnFragmentInteractionListener?
        else throw RuntimeException((context!!.toString() + " must implement OnFragmentInteractionListener"))
    }

    public override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    //TODO FIXME XXX Add onclicklisteners to all checkboxes, that run a function that does this?
    private fun layoutOnClickListener(layout: LinearLayout?) {
        layout?.setOnClickListener {
            var i: Int = 0
            var remove = true
            Log.i("ChildCount", layout.childCount.toString())
            while(i < layout.childCount) {
                var a: CheckBox = layout?.getChildAt(i) as CheckBox
                if(!a.isChecked) {
                    remove = false
                    break
                }
                i++
            }
            if(remove) {
                layout?.animate().translationX(layout.width.toFloat())
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
}// Required empty public constructor
