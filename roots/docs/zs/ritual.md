
### Class

```zenscript
import mods.roots.Ritual;
```

#### Methods

```zenscript
Ritual setDouble(
  string propertyName, // sets propertyName to the specified double value
  double value         // the value to set propertyName to; if this property is *not* a double, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setFloat(
  string propertyName, // sets propertyName to the specified float value
  float value          // the value to set propertyName to; if this property is *not* a float, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setInteger(
  string propertyName, // sets propertyName to the specified integer value
  int value            // the value to set propertyName to; if this property is *not* a integer, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setDuration(
  int value // the new duration for the ritual
);
```

Changes the duration of the ritual and returns the Ritual object for further modification. Is shorthand for `setInteger("duration", value)`.

---


```zenscript
Ritual setString(
  string propertyName, // sets propertyName to the specified string value
  string value         // the value to set propertyName to; if this property is *not* a string, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


