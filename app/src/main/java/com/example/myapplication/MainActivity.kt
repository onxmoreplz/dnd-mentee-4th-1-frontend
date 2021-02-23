package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.navigation.feed.FeedFragment
import com.example.myapplication.navigation.home.HomeFragment
import com.example.myapplication.navigation.mypage.MyPageFragment
import com.example.myapplication.navigation.search.SearchFragment
import com.example.myapplication.navigation.upload.UploadActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val homeFragment by lazy { HomeFragment() }
    private val feedFragment by lazy { FeedFragment() }
    private val searchFragment by lazy { SearchFragment() }
    private val myPageFragment by lazy { MyPageFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setStatusBarTransparent()


        initBottomNavigation()



    }

    fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    /**
     *  <p> 하단 네비게이션 이벤트 처리 함수 </p>
     *
     */
    private fun initBottomNavigation() {
        bnv_home.background=null
        bnv_home.menu.getItem(2).isEnabled = false
        bnv_home.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        setFragment(homeFragment)
                    }
                    R.id.feed -> {
                        setFragment(feedFragment)
                    }
                    R.id.search -> {
                        setFragment(searchFragment)
                    }
                    R.id.myPage -> {
                        setFragment(myPageFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home // 초기 프래그먼트
        }

        fab_write.setOnClickListener {
            val intent = Intent(App.instance, UploadActivity::class.java)
            startActivity(intent)
        }

    }

    /**
     *  <p> 프래그먼트 전환 함수 </p>
     *
     * @param fragment: Fragment 전환될 프래그먼트
     */
    private fun setFragment(fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        manager.beginTransaction().setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
            .replace(R.id.fl_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}