package com.mathcamp.fiberglass

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Created by bkase on 6/21/16.
 */

private object DateGetterSetter {
  val dates by lazy {
    object: GetterSetter<Date> {
      override fun get(prefs: SharedPreferences, name: String): Date =
          Date(prefs.getLong(name, System.currentTimeMillis()))

      override fun put(edit: SharedPreferences.Editor, name: String, value: Date) {
        edit.putLong(name, value.time)
      }
    }
  }
}

fun DateSavable(namespace: String, ctx: Context): Savable<Date> =
    Savable(namespace, ctx, DateGetterSetter.dates)
