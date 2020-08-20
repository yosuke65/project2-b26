package com.example.project2.ui.main.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.project2.R
import com.example.project2.databinding.ActivityHomeBinding
import com.example.project2.ui.auth.login.LoginActivity
import com.example.project2.ui.main.activities.CollectRentActivity
import com.example.project2.ui.main.activities.DocumentActivity
import com.example.project2.ui.main.activities.TenantActivity
import com.example.project2.ui.main.activities.TransactionActivity
import com.example.project2.ui.main.property.addproperty.AddPropertyActivity
import com.example.project2.ui.main.property.property.PropertyActivity
import com.example.project2.ui.main.todo.ToDoActivity
import com.example.project2.utils.PreferenceManager
import com.example.project2.utils.ProgressBarAnimation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_collapsing_add_property.*
import kotlinx.android.synthetic.main.toolbar_collapsing_home.*
import kotlinx.android.synthetic.main.toolbar_collapsing_home.button_home
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main_menu.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var revealLayout: View
    private lateinit var fab: FloatingActionButton
    private lateinit var mainHandler: Handler

    companion object {
        private const val TAG = "HomeActivity"
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)

        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {

        //DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.item = this

        //menu
        revealLayout = home_layout
        fab = floating_action_button_menu


        button_main_setting.setOnClickListener {
            showPopup()
        }
        //setup username in home
        setUsername()

        //toolbar
        setUpToolbar()


    }

    override fun onResume() {
        super.onResume()
        //Progressbar for occupancy
        setOccupancy()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPause() {
        super.onPause()
        hideMenu()
    }
    

    private fun setOccupancy() {
        //temp
        var tenants = 3
        var untenants = 2
        var totalTenants = tenants.toDouble() + untenants.toDouble()
        val percentage: Int
        if (totalTenants <= 0) {
            percentage = 0
        } else {
            percentage = (tenants / totalTenants * 100).toInt()
        }
        text_view_tenanted_count.text = tenants.toString()
        text_view_untenanted_count.text = untenants.toString()
        startProgressAnimation(percentage)

    }

    private fun startProgressAnimation(percentage: Int) {
        var anim =
            ProgressBarAnimation(progress_bar_percentage_indicator, 0.0f, percentage.toFloat())
        anim.duration = 2000
//        anim.setInterpolator(this, android.R.anim.decelerate_interpolator)
        progress_bar_percentage_indicator.startAnimation(anim)
        val valueAnim = ValueAnimator.ofInt(0, percentage)
        valueAnim.duration = 3000
//        valueAnim.setInterpolator(DecelerateInterpolator())
        valueAnim.addUpdateListener {
            text_view_percentage_occupancy.text = "${valueAnim.animatedValue}%"
        }
        valueAnim.start()
    }


    private fun setUpToolbar() {
        toolbar_collapsing_home.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1
            var position = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                Log.v(TAG, "ScrollRange: $scrollRange, VerticalOffset: $verticalOffset, Radius = ")
                toolbar_collapsing_home.translationY = (verticalOffset / 4).toFloat()
                if (scrollRange + verticalOffset == 0) {
                    toolbar_second_home.visibility = View.VISIBLE
                    toolbar_second_home.animate()
                        .alpha(1.0f).also {
                        it.duration = 500
                        it.start()
                    }
                }
                //Scroll down
                if(position > -verticalOffset ){
                    Log.d(TAG, "onOffsetChanged: Scroll Down")
                    toolbar_second_home.animate().alpha(0f).also {
                        it.duration = 100
                        it.start()
                    }
                }
                position = -verticalOffset
            }
        })
    }

    private fun setUsername() {
        var username = PreferenceManager.getPreference(PreferenceManager.USERNAME, "")
        text_view_user_name.text = "$username"
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun toggleMenu() {

        if (reveal_menu.visibility == View.INVISIBLE) {

            showMenu()

        } else {

            hideMenu()

        }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideMenu() {
        var x = revealLayout.right
        var y = revealLayout.bottom - 100
        var centerHorizontal = revealLayout.width / 2 - 130
        var startRadius = 0.0f
        var endRadius = Math.max(reveal_menu.width, reveal_menu.height) * 2.0f
        Handler().postDelayed({
            var revealAnimator =
                ViewAnimationUtils.createCircularReveal(
                    reveal_menu,
                    revealLayout.width / 2,
                    y,
                    endRadius,
                    0.0f
                )
            revealAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {

                    fabSlideOut(centerHorizontal)
                    toolbar_second_home.translationZ = 1f
                    reveal_menu.translationZ = -1f
                    reveal_menu.visibility = View.INVISIBLE
                }
            })
            revealAnimator.start()
        }, 300)

        fabIconCloseTransition()
        menuIconFadeSlideOut()
        toolbarSlideOut()

    }

    private fun fabSlideOut(centerHorizontal: Int) {
        fab.animate().translationXBy(centerHorizontal.toFloat()).also {
            it.duration = 500
            it.start()
        }
    }

    private fun toolbarSlideOut() {
        toolbar_main_menu.animate().alpha(0.0f)
            .translationYBy(-toolbar_main_menu.height.toFloat()).also {
                it.duration = 400
                it.start()
            }
    }

    private fun menuIconFadeSlideOut() {
        menu_container.animate().alpha(0.0f).translationYBy(100.0f).also {
            it.duration = 400
            it.start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fabIconCloseTransition() {
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.anim_menu_close));
        var anim = fab.drawable as AnimatedVectorDrawable
        anim.start()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showMenu() {


        var x = revealLayout.right
        var y = revealLayout.bottom - 100
        var centerHorizontal = revealLayout.width / 2 - 130
        var startRadius = 0.0f
        var endRadius = Math.max(reveal_menu.width, reveal_menu.height) * 2.0f

        toolbar_second_home.translationZ = -1f
        reveal_menu.translationZ = 1f
        reveal_menu.visibility = View.VISIBLE
        fabIconOpenTransition()

        ViewAnimationUtils.createCircularReveal(reveal_menu, x, y, startRadius, endRadius).start()
        fabSlideIn(centerHorizontal)
        toolbarSlideOIn()
        menuIconFadeSlideIn()
    }

    private fun menuIconFadeSlideIn() {
        menu_container.animate().alpha(1.0f).translationYBy(-100.0f).also {
            it.duration = 700
            it.start()
        }
    }

    private fun toolbarSlideOIn() {
        toolbar_main_menu.animate().alpha(1.0f)
            .translationYBy(toolbar_main_menu.height.toFloat()).also {
                it.duration = 700
            }.start()
    }

    private fun fabSlideIn(centerHorizontal: Int) {
        fab.animate().translationXBy(-centerHorizontal.toFloat()).also {
            it.duration = 700
            it.start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fabIconOpenTransition() {
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.anim_menu_open));
        var anim = fab.drawable as AnimatedVectorDrawable
        anim.start()
    }

    fun startPropertyActivity() {
        startActivity(Intent(this, PropertyActivity::class.java))
    }

    fun startTenantActivity() {
        startActivity(Intent(this, TenantActivity::class.java))
    }

    fun startTransactionActivity() {
        startActivity(Intent(this, TransactionActivity::class.java))
    }

    fun startCollectRentActivity() {
        startActivity(Intent(this, CollectRentActivity::class.java))
    }

    fun startDocumentActivity() {
        startActivity(Intent(this, DocumentActivity::class.java))
    }

    fun startToDoActivity() {
        startActivity(Intent(this, ToDoActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                showPopup()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopup() {
//        val menuView = findViewById<View>(R.id.menu_setting)
//        val popup = PopupMenu(this,menuView)
        var popup = PopupMenu(this, button_main_setting)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    PreferenceManager.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
        popup.inflate(R.menu.menu_main_popup)
        popup.show()
    }

}