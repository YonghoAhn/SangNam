package top.mikoto.sangnam.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import top.mikoto.sangnam.R
import top.mikoto.sangnam.utils.PrefManager
import java.util.*

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var layouts: IntArray
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch) {
            launchHomeScreen()
            finish()
        }

// Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        val actionBar = supportActionBar
        actionBar?.hide()
        setContentView(R.layout.activity_welcome)
        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)
        layouts = intArrayOf(
                R.layout.welcome1,
                R.layout.welcome2,
                R.layout.welcome3,
                R.layout.welcome4)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()
        val myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.setAdapter(myViewPagerAdapter)
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        btnSkip.setOnClickListener(View.OnClickListener { v: View? -> launchHomeScreen() })
        btnNext.setOnClickListener(View.OnClickListener { v: View? ->
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                viewPager.setCurrentItem(current)
            } else {
                launchHomeScreen()
            }
        })
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        prefManager!!.isFirstTimeLaunch = false
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    private val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                btnNext!!.text = getString(R.string.start)
                btnSkip!!.visibility = View.GONE
            } else {
                // still pages are left
                btnNext!!.text = getString(R.string.next)
                btnSkip!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    internal inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = Objects.requireNonNull(layoutInflater)!!.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}