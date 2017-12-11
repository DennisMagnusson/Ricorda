package tech.dennismagnusson.studyjournal

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import tech.dennismagnusson.studyjournal.R
import java.io.FileReader


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReadFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReadFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var layout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater!!.inflate(R.layout.fragment_read, container, false)

        layout = view?.findViewById(R.id.readLayout)

        val filename = context.filesDir.path + "/.studyJournal"
        val reader = FileReader(filename)

        val dateFormatLength = getString(R.string.date_format).length
        var date = ""
        var cardLayout: LinearLayout? = null
        var cardView: CardView? = null
        for(line in reader.readLines().reversed()) {
            var str = line
            val dateStr = str.substring(0, dateFormatLength)
            if(dateStr != date) {
                date = dateStr
                if(cardLayout != null) {
                    layout?.addView(cardView)
                    var space = Space(activity)
                    space.minimumHeight = resources.getDimension(R.dimen.card_vertical_margin).toInt()
                    layout?.addView(space)
                }
                cardView = createCardView(dateStr)
                cardLayout = cardView.getChildAt(0) as LinearLayout
            }
            str = str.substring(dateFormatLength+1)
            if(str == "") continue
            cardLayout?.addView(createTextView(str))
        }

        if(cardLayout != null) {
            layout?.addView(cardView)
            var space = Space(activity)
            space.minimumHeight = resources.getDimension(R.dimen.card_vertical_margin).toInt()
            layout?.addView(space)
        } else {
            val textView = TextView(activity)
            textView.text = getString(R.string.empty_journal_text)
            textView.textSize = resources.getDimension(R.dimen.read_skill_text_size)
            layout?.addView(textView)
        }

        return view
    }

    private fun createTextView(text: String): TextView {
        var textView = TextView(activity)
        textView.text = text
        textView.textSize = resources.getDimension(R.dimen.read_date_text_size)
        return textView
    }

    private fun createCardView(date: String): CardView {
        var card = CardView(activity)
        var layout = LinearLayout(activity)
        layout.orientation = LinearLayout.VERTICAL

        var textView = TextView(activity)
        textView.text = date
        textView.setTextColor(Color.GRAY)

        layout.addView(textView)
        card.addView(layout)

        return card
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
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
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ReadFragment {
            val fragment = ReadFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
