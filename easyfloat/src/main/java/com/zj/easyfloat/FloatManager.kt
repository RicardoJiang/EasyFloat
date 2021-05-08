package com.zj.easyfloat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.zj.easyfloat.floatingview.EnFloatingView
import com.zj.easyfloat.floatingview.FloatingMagnetView
import com.zj.easyfloat.floatingview.FloatingView
import com.zj.easyfloat.floatingview.MagnetViewListener

object FloatManager : Application.ActivityLifecycleCallbacks {
    private var mLayoutParams = getFloatingLayoutParams()
    private val blackList = mutableListOf<Class<*>>()
    private var mLayout: Int = 0
    private var mListener: ((View?) -> Unit)? = null

    fun layout(layout: Int): FloatManager {
        mLayout = layout
        return this
    }

    fun layoutParams(layoutParams: FrameLayout.LayoutParams): FloatManager {
        mLayoutParams = layoutParams
        return this
    }

    fun blackList(blackList: MutableList<Class<*>>): FloatManager {
        FloatManager.blackList.addAll(blackList)
        return this
    }

    fun listener(listener: ((View?) -> Unit)): FloatManager {
        mListener = listener
        return this
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        if (isActivityInValid(activity)) {
            return
        }
        initShow(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (isActivityInValid(activity)) {
            return
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (isActivityInValid(activity)) {
            return
        }
        FloatingView.get().detach(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    private fun isActivityInValid(activity: Activity): Boolean {
        return blackList.contains(activity::class.java)
    }

    private fun getFloatingLayoutParams(): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.START
        params.setMargins(0, params.topMargin, params.rightMargin, 500)
        return params
    }

    private fun initShow(activity: Activity) {
        activity.let {
            if (FloatingView.get().view==null){
                FloatingView.get().customView(
                    EnFloatingView(activity, mLayout)
                )
            }
            FloatingView.get().layoutParams(mLayoutParams)
            FloatingView.get().attach(it)
            mListener?.invoke(FloatingView.get().view)
        }
    }

    fun show(activity: Activity) {
        initShow(activity)
        activity.application.registerActivityLifecycleCallbacks(this)
    }

    fun dismiss(activity: Activity) {
        FloatingView.get().remove()
        FloatingView.get().detach(activity)
        activity.application.unregisterActivityLifecycleCallbacks(this)
    }

    private fun initListener() {
        FloatingView.get().listener(object : MagnetViewListener {
            override fun onRemove(magnetView: FloatingMagnetView?) {

            }

            override fun onClick(magnetView: FloatingMagnetView?) {
                mListener?.invoke(magnetView)
            }

        })
    }
}