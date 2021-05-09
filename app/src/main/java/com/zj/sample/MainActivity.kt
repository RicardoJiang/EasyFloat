package com.zj.sample

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zj.easyfloat.FloatManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFloat()
    }

    private fun initFloat() {
        FloatManager.layout(R.layout.layout_float_view)
            .blackList(mutableListOf(ThirdActivity::class.java))
            .layoutParams(initLayoutParams())
            .listener {
                initListener(it)
            }
            .show(this)
    }

    private fun initLayoutParams(): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.setMargins(0, params.topMargin, params.rightMargin, 500)
        return params
    }

    private fun initListener(root: View?) {
        val rootView = root?.findViewById<View>(R.id.ll_root)
        root?.findViewById<View>(R.id.floating_ball)?.setOnClickListener {
            if (rootView != null) {
                if (rootView.getTag(R.id.animate_type) == null) {
                    rootView.setTag(R.id.animate_type, true)
                }
                val isCollapse = rootView.getTag(R.id.animate_type) as Boolean
                animScale(rootView, isCollapse)
                rootView.setTag(R.id.animate_type, isCollapse.not())
            }
        }
        root?.findViewById<View>(R.id.floating_ball_one)?.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }
    }

    fun show(view: View) {
        FloatManager.show(this)
    }

    fun dismiss(view: View) {
        FloatManager.dismiss(this)
    }

    fun jumpOne(view: View) {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

    fun jumpTwo(view: View) {
        val intent = Intent(this, ThirdActivity::class.java)
        startActivity(intent)
    }

    private fun animScale(view: View, isCollapse: Boolean) {
        val start = if (isCollapse) dip2px(172f) else dip2px(60f)
        val end = if (isCollapse) dip2px(60f) else dip2px(172f)

        val scaleBig = ValueAnimator.ofFloat(start, end)
        scaleBig.duration = 1000
        scaleBig.addUpdateListener {
            val layoutParams = view.layoutParams
            layoutParams.height = (it.animatedValue as Float).toInt()
            view.layoutParams = layoutParams
        }
        scaleBig.start()
    }

    private fun dip2px(dpValue: Float): Float {
        val scale: Float = getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f)
    }
}