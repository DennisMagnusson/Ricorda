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
    public var yesterday: LinearLayout? = null
    public var week: LinearLayout? = null
    public var month: LinearLayout?= null
    public var year: LinearLayout? = null

    private lateinit var res: android.content.res.Resources
    public var height: Int = -1

    //TODO  Make private
    private fun createCheckBox(text:String, ctx: Context) : CheckBox {
        val checkBox = CheckBox(ctx)
        //val checkBox: CheckBox = CheckBox(activity.applicationContext)
        checkBox.isChecked = false
        checkBox.text = text
        checkBox.width = ActionBar.LayoutParams.MATCH_PARENT
        //checkBox.height= resources.getDimension(R.dimen.repeat_checkbox_height).toInt()
        checkBox.height = 24
        Log.i("ASDLFAJSDFLKASF", height.toString())
        //checkBox.height = height
        //checkBox.height = res.getDimension(R.dimen.repeat_checkbox_height).toInt()
        checkBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
        return checkBox
    }

    public fun addCheckBox(l: LinearLayout?, text: String, ctx: Context) {
        l?.addView(createCheckBox(text, ctx))
    }

    public override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            text = getArguments().getString("text")
        }

        res = resources
        height = res.getDimension(R.dimen.repeat_checkbox_height).toInt()
        Log.i("THIS IS IN ONCREATE", height.toString())

    }

    public override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
                                     savedInstanceState:Bundle?):View? {
        // Inflate the layout for this fragment

        Log.i("THIS IS IN ONCREATEVIEW", height.toString())
        return inflater!!.inflate(R.layout.fragment_repeat, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
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
            fragment.yesterday = fragment.view?.findViewById(R.id.yesterdayLayout)
            fragment.week = fragment.view?.findViewById(R.id.weekLayout)
            fragment.month = fragment.view?.findViewById(R.id.monthLayout)
            fragment.year = fragment.view?.findViewById(R.id.yearLayout)
            return fragment
        }

    }
}// Required empty public constructor
