package com.mathcamp.fiberglass

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Defaults {
  val strings = ""
  val ints = Int.MIN_VALUE
  val longs = Long.MIN_VALUE
  val doubles = Double.MIN_VALUE
  val floats = Float.MIN_VALUE
  val bools = false
}

interface GetterSetter<T> {
  fun get(prefs: SharedPreferences, name: String): T
  fun put(edit: SharedPreferences.Editor, name: String, value: T)
}

private object GetterSetters {
  val strings by lazy {
    object : GetterSetter<String> {
      override fun get(prefs: SharedPreferences, name: String): String =
        prefs.getString(name, Defaults.strings)

      override fun put(edit: SharedPreferences.Editor, name: String, value: String) {
        edit.putString(name, value)
      }
    }
  }

  val ints by lazy {
    object : GetterSetter<Int> {
      override fun get(prefs: SharedPreferences, name: String): Int =
        prefs.getInt(name, Defaults.ints)

      override fun put(edit: SharedPreferences.Editor, name: String, value: Int) {
        edit.putInt(name, value)
      }
    }
  }

  val longs by lazy {
    object : GetterSetter<Long> {
      override fun get(prefs: SharedPreferences, name: String): Long =
        prefs.getLong(name, Defaults.longs)

      override fun put(edit: SharedPreferences.Editor, name: String, value: Long) {
        edit.putLong(name, value)
      }
    }
  }

  val doubles by lazy {
    object : GetterSetter<Double> {
      override fun get(prefs: SharedPreferences, name: String): Double {
        val def = java.lang.Double.doubleToRawLongBits(Defaults.doubles)
        val l = prefs.getLong(name, def)
        return java.lang.Double.longBitsToDouble(l)
      }

      override fun put(edit: SharedPreferences.Editor, name: String, value: Double) {
        edit.putLong(name, java.lang.Double.doubleToRawLongBits(value))
      }
    }
  }

  val floats by lazy {
    object: GetterSetter<Float> {
      override fun get(prefs: SharedPreferences, name: String): Float =
        prefs.getFloat(name, Defaults.floats)

      override fun put(edit: SharedPreferences.Editor, name: String, value: Float) {
        edit.putFloat(name, value)
      }
    }
  }

  val bools by lazy {
    object: GetterSetter<Boolean> {
      override fun get(prefs: SharedPreferences, name: String): Boolean =
        prefs.getBoolean(name, Defaults.bools)

      override fun put(edit: SharedPreferences.Editor, name: String, value: Boolean) {
        edit.putInt(name, if (value) { 1 } else { 0 })
      }
    }
  }
}

fun StringSavable(namespace: String, ctx: Context): Savable<String> =
    Savable(namespace, ctx, GetterSetters.strings)

fun IntSavable(namespace: String, ctx: Context): Savable<Int> =
    Savable(namespace, ctx, GetterSetters.ints)

fun LongSavable(namespace: String, ctx: Context): Savable<Long> =
    Savable(namespace, ctx, GetterSetters.longs)

fun DoubleSavable(namespace: String, ctx: Context): Savable<Double> =
    Savable(namespace, ctx, GetterSetters.doubles)

fun FloatSavable(namespace: String, ctx: Context): Savable<Float> =
    Savable(namespace, ctx, GetterSetters.floats)

fun BoolSavable(namespace: String, ctx: Context): Savable<Boolean> =
    Savable(namespace, ctx, GetterSetters.bools)

class Savable<T>(
    val namespace: String,
    val ctx: Context,
    val getterSetter: GetterSetter<T>
): ReadWriteProperty<Any?, T> {
  private fun prefs(ctx: Context): SharedPreferences =
    ctx.getSharedPreferences(namespace, Context.MODE_PRIVATE)

  override fun getValue(thisRef: Any?, property: KProperty<*>): T =
    getterSetter.get(prefs(ctx), property.name)

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    val editor = prefs(ctx).edit()
    getterSetter.put(editor, property.name, value)
    editor.apply()
  }
}