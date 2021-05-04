package com.zj.floatlibrary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.imuxuan.floatingview.EnFloatingView
import com.imuxuan.floatingview.FloatingMagnetView
import com.imuxuan.floatingview.FloatingView
import com.imuxuan.floatingview.MagnetViewListener

object FloatManager : Application.ActivityLifecycleCallbacks {
    private var translationX = 0f
    private var translationY = 0f

    private var mLayoutParams = getFloatingLayoutParams()
    private val blackList = mutableListOf<Class<*>>()
    private var mLayout: Int = 0
    private var mListener: ((View?) -> Unit)? = null

    fun layout(layout: Int): FloatManager {
        this.mLayout = layout
        return this
    }

    fun layoutParams(layoutParams: FrameLayout.LayoutParams): FloatManager {
        this.mLayoutParams = layoutParams
        return this
    }

    fun blackList(blackList: MutableList<Class<*>>): FloatManager {
        this.blackList.addAll(blackList)
        return this
    }

    fun listener(listener: ((View?) -> Unit)): FloatManager {
        this.mListener = listener
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
        FloatingView.get().add()
    }

    override fun onActivityPaused(activity: Activity) {
        if (isActivityInValid(activity)) {
            return
        }
        FloatingView.get().view?.let {
            translationX = it.translationX
            translationY = it.translationY
        }
        FloatingView.get().remove()
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
        if (FloatingView.get().view != null) {
            return
        }
        activity.let {
            FloatingView.get().customView(
                EnFloatingView(activity, mLayout)
            )
            FloatingView.get().layoutParams(mLayoutParams)
            FloatingView.get().attach(it)
            FloatingView.get()?.view?.let { view ->
                view.translationX = translationX
                view.translationY = translationY
            }
            initListener()
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