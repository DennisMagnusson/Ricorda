package com.example.d.studyjournal

import android.content.Intent
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainActivity : AppCompatActivity(), RepeatFragment.OnFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener, ReadFragment.OnFragmentInteractionListener {

    //Fuck this shit
    override fun onFragmentInteraction(uri: Uri) {
    }

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var repeatFragment: RepeatFragment
    private var calFragment: Fragment? = null
    private val REPEAT_INDEX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        repeatFragment = mSectionsPagerAdapter?.getItem(REPEAT_INDEX) as RepeatFragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1)
            if(position == 0) return RepeatFragment.newInstance("f")
            if(position == 1) return ReadFragment.newInstance("", "")
            //if(position == 1) return CalendarFragment.newInstance("F", "temp")
            //if(position == 1) return PlaceholderFragment.newInstance(position+1)

            return RepeatFragment.newInstance("1337")
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
