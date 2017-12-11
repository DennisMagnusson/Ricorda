package tech.dennismagnusson.ricorda

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(prefs.getBoolean("night_switch", false))
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

        if(prefs.getBoolean("notification_switch", false)) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 21)
            calendar.set(Calendar.MINUTE, 0)
            calendar.add(Calendar.DAY_OF_YEAR, 1)

            val notificationIntent = Intent(this@MainActivity, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, notificationIntent, 0)

            val alarmMan = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmMan.set(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
        }


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
            startActivity(Intent(this, SettingsActivity::class.java))
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
