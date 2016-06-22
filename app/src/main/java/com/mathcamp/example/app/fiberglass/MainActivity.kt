package com.mathcamp.example.app.fiberglass

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.mathcamp.fiberglass.DateSavable
import com.mathcamp.fiberglass.LongSavable
import com.mathcamp.fiberglass.StringSavable
import java.util.*

class User(val ctx: Context) {
  companion object {
    private val namespace = "user"
  }

  var phone by LongSavable(namespace, ctx)
  var email by StringSavable(namespace, ctx)
  var lastSaved by DateSavable(namespace, ctx)
}

class MainActivity : AppCompatActivity() {
  private val phoneEditText by lazy {
    findViewById(R.id.phoneEditText) as EditText
  }

  private val emailEditText by lazy {
    findViewById(R.id.emailEditText) as EditText
  }

  private val saveButton by lazy {
    findViewById(R.id.saveButton) as Button
  }

  private fun <T> setEditText(editText: EditText, data: T) {
    editText.text.apply {
      clear()
      append(data.toString())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val u = User(this)

    supportActionBar?.setHomeButtonEnabled(true)

    val p = u.phone
    setEditText(phoneEditText, if (p == Long.MIN_VALUE) { "" } else { p.toString() })
    setEditText(emailEditText, u.email)

    saveButton.setOnClickListener{ done() }
  }

  private fun done() {
    val u = User(this)
    u.phone = phoneEditText.text.toString().toLong()
    u.email = emailEditText.text.toString()

    Date().let{
      Snackbar.make(saveButton, "Saved -- try force quitting and reopening", Snackbar.LENGTH_LONG)
      u.lastSaved = it
      finish()
    }
  }

  override fun onBackPressed() {
    done()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    Log.d(MainActivity::class.java.name, "Selected item $item")
    Log.d(MainActivity::class.java.name, "Home is ${android.R.id.home}")
    return when (item.itemId) {
      android.R.id.home -> {
        done()
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }
}
