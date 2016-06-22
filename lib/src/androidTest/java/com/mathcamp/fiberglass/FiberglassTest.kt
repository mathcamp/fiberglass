package com.mathcamp.fiberglass

import android.app.Application
import android.content.Context
import android.test.ApplicationTestCase

/**
 * Created by bkase on 6/20/16.
 */

class User(ctx: Context) {
  private val namespace = "user"

  var name by StringSavable(namespace, ctx)
  var age by IntSavable(namespace, ctx)
  var createdAt by LongSavable(namespace, ctx)
  var height by DoubleSavable(namespace, ctx)
  var weight by FloatSavable(namespace, ctx)
  var isLoggedIn by BoolSavable(namespace, ctx)
}

class FiberglassTest: ApplicationTestCase<Application>(Application::class.java) {
  fun testFiberglass() {
    val ts = System.currentTimeMillis()
    with(User(context)) {
      age = 5
      name = "Fred"
      createdAt = ts
      height = 1.3
      weight = 1.5F
      isLoggedIn = true
    }

    with(User(context)) {
      assert(age == 5)
      assert(name == "Fred")
      assert(createdAt == ts)
      assert(height == 1.3)
      assert(weight == 1.5F)
      assert(isLoggedIn)
    }
  }

  fun testDefaults() {
    val u = User(context)
    assert(u.age == -1)
  }
}