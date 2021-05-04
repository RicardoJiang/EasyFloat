package com.zj.easyfloat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zj.floatlibrary.FloatManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFloat()
    }

    private fun initFloat(){
        FloatManager.layout(R.layout.layout_float_view)
            .blackList(mutableListOf(ThirdActivity::class.java))
            .listener {
                Log.i("tiaoshi","here")
            }
            .show(this)
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
}