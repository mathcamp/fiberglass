package com.mathcamp.example.app.fiberglass

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView

class ScrollingActivity : AppCompatActivity() {
  private val toolbar by lazy {
    findViewById(R.id.toolbar) as Toolbar
  }

  private val fab by lazy {
    findViewById(R.id.fab) as FloatingActionButton
  }

  private val phoneText by lazy {
    findViewById(R.id.phoneText) as TextView
  }

  private val emailText by lazy {
    findViewById(R.id.emailText) as TextView
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scrolling)
    toolbar.title = ""
    setSupportActionBar(toolbar)
    supportActionBar?.title = "Sample Profile"

    fab.setOnClickListener{ view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
      startActivity(Intent(ScrollingActivity@this, MainActivity::class.java))
    }
  }

  override fun onResume() {
    super.onResume()

    val u = User(this)
    u.phone.let {
      if (it != Long.MIN_VALUE) {
        phoneText.text = it.toString()
      }
    }
    u.email.let{
      if (it != "") {
        emailText.text = it
      }
    }
  }
}
