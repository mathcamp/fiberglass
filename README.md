# Fiberglass

![fiberglass app icon](http://i.imgur.com/2Z7oYMX.png)
![fiberglass logo](http://imgur.com/NHmDzWW.png)

Easy lightweight SharedPreferences library for Android in Kotlin using delegated properties

## Idea

Delegated properties in Kotlin allow you to execute arbitrary code whenever a field is accessed. The cool thing about delegated properties is that we can package up the code and reuse it.

Let's package up the notion of getting from SharedPreferences and writing to SharedPreferences.

## Usage

Just stick metadata in your classes with specific delegated properties

```kotlin
class User(val ctx: Context) {
  companion object {
    private val namespace = "user"
  }

  var email by StringSavable(namespace, ctx)
  var lastSaved by DateSavable(namespace, ctx)
  var phone by LongSavable(namespace, ctx)
}
```

Get and set just like normal field properties

```kotlin
val u = User(this)
/* ... */

// Get
val greetings = "Hello ${u.name}"

/* ... */

// Set
u.name = "Brandon"
```

If you want to save custom a custom data type, you just have to provide a way to serialize it to SharedPreferences

```kotlin
private object DateGetterSetter {
  // make it a lazy object to reuse the same inner-class for each savable
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
```

## Example App

See the `app` module for a sample app

![screenshot](http://i.imgur.com/3cp5jI7.png)

## Default values

Shared preferences requires default values when the data is missing. The default values for the builtin savables are:

| Type  | Default Value |
| ------------- | ------------- |
| StringSavable  | "" |
| IntSavable  | Int.MIN_VALUE |
| LongSavable  | Long.MIN_VALUE |
| DoubleSavable  | Double.MIN_VALUE |
| FloatSavable  | Float.MIN_VALUE |
| BooleanSavable  | false |
| DateSavable  | Date now  |

## Consistency

Savable field writes are eventually consistent (implemented with `apply()` on the shared preferences editor).

It would make sense to also support consistent writes (implemented with `commit()` on the shared preferences editor) somehow. Please send a PR or open an issue with ideas.

