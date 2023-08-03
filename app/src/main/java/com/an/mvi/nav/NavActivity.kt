package com.an.mvi.nav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.an.mvi.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/***
 *
 * 属性说明：
 *  singleTop: Boolean	        app:launchSingleTop="<boolean>"	    保证返回栈栈顶只有一个目的地实例。类似Activity的singleTop启动模式
 *  popUpToId: Integer	        app:popUpTo="@id/<目的地id>"	        将返回栈中popUpToId（默认不包括自己）之上的所有目的地弹出
 *  popUpToInclusive: Boolean	app:popUpToInclusive="<boolean>"	与popUpToId配套使用，true表示popUpToId自己也要弹出返回栈
 *  popUpToSaveState: Boolean	app:popUpToSaveState="<boolean>"	与popUpToId配套使用，true表示保存所有弹出的目的地的状态
 *  restoreState: Boolean	    app:restoreState="<boolean>"	    true表示恢复之前保存的目的地状态
 *
 * 方法说明：
 * navigateUp() vs popBackStack()
 * 1-   应用本身界面跳转到当前界面，则两者都是返回上一界面，无区别
 * 2-   其他应用通过deep link跳转到当前应用的界面：
 *      navigateUp()会返回其他应用，
 *      popBackStack()则尝试返回当前应用的栈内上一个界面
 *
 * 需求场景（ =>代表使用底部导航栏切换,->代表使用代码跳转 ）：
 *  1-  A->Process->Result->A   要求回到A时，Process Result退出栈
 *  2-  A->Process->Result->B   要求跳转B时，Process Result退出栈
 *  3-  A=>B->C=>A              要求切换到A时，C退出栈，且过程中导航栏不隐藏
 * ***/
class NavActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        initView()
        initListener()
        collectCurrentStack()
    }


    private fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navigationView = findViewById(R.id.bottom_nav)
        navigationView.setupWithNavController(navController)
    }

    private fun initListener() {
        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.a -> {
                    navController.popBackStack()
                    navController.navigate(
                        R.id.a,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true)
                            .setEnterAnim(R.animator.nav_default_pop_enter_anim)
                            .setExitAnim(R.animator.nav_default_pop_exit_anim)
                            .build()
                    )
                    true
                }
                R.id.b -> {
                    navController.popBackStack()
                    navController.navigate(
                        R.id.b,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true)
                            .setEnterAnim(R.animator.nav_default_enter_anim)
                            .setExitAnim(R.animator.nav_default_exit_anim)
                            .build()
                    )
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.AFragment -> {
                    navigationView.visibility = View.VISIBLE
                }
                R.id.BFragment -> {
                    navigationView.visibility = View.VISIBLE
                }
                R.id.CFragment -> {
                    navigationView.visibility = View.VISIBLE
                }
                else -> {
                    navigationView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 观察当前栈内有多少个Fragment
     * **/
    private fun collectCurrentStack() {
        lifecycleScope.launch {
            navController.currentBackStack.collectLatest { stackEntry ->
                stackEntry.forEach {
                    Log.d(TAG, it.toString())
                }
            }
        }
    }

    companion object {
        const val TAG = "NavActivity"
    }
}