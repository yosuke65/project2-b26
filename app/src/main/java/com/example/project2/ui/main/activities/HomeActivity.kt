package com.example.project2.ui.main.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.project2.R
import com.example.project2.databinding.ActivityHomeBinding
import com.example.project2.ui.main.fragment.BottomSheetFragment
import com.example.project2.ui.main.todo.ToDoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var bottomSheetFragment = BottomSheetFragment()
    private lateinit var binding:ActivityHomeBinding
    private lateinit var revealLayout: View
    private lateinit var floatingActionButton:FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)

        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {

        //DataBinding
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        binding.item = this

        //menu
        revealLayout = home_layout
        floatingActionButton =floating_action_button_menu
        floating_action_button_menu.setOnClickListener{
            //showBottomSheetMenu()
            revealMenu(revealLayout)
        }

        //toolbar
//        var toolbar = toolbar_main
//        toolbar.title = "Home"
//        toolbar.setTitleTextColor(Color.WHITE)
//        setSupportActionBar(toolbar)

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun revealMenu(view:View) {

        var x = view.right
        var y = view.bottom
        var endRadius = Math.max(reveal_menu.width, reveal_menu.height) *2.0f
        if(reveal_menu.visibility == View.INVISIBLE){
            reveal_menu.visibility = View.VISIBLE
            ViewAnimationUtils.createCircularReveal(reveal_menu,x,y,0.0f,endRadius).start()
        }else{
            var revealAnimator = ViewAnimationUtils.createCircularReveal(reveal_menu,x,y,endRadius,0.0f)
            revealAnimator.addListener(object:AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    reveal_menu.visibility = View.INVISIBLE
                }
            })
            revealAnimator.start()


        }

    }

    fun startPropertyActivity(){
        startActivity(Intent(this,PropertyActivity::class.java))
    }
    fun startTenantActivity(){
        startActivity(Intent(this,TenantActivity::class.java))
    }
    fun startTransactionActivity(){
        startActivity(Intent(this,TransactionActivity::class.java))
    }
    fun startCollectRentActivity(){
        startActivity(Intent(this,CollectRentActivity::class.java))
    }
    fun startToDoActivity(){
        startActivity(Intent(this,
            ToDoActivity::class.java))
    }
    fun startDocumentActivity(){
        startActivity(Intent(this,DocumentActivity::class.java))
    }

    private fun showBottomSheetMenu() {
        bottomSheetFragment.show(supportFragmentManager,bottomSheetFragment.tag)
    }
}