
### Class

```zenscript
import mods.roots.Spell;
```

#### Methods

```zenscript
Spell setDouble(
  string propertyName, // the name of the property (must be a double)
  double value         // the value to be inserted for this property (must be a double)
);
```

If the property is not of the double type, an error will occur.

---


```zenscript
Spell setFloat(
  string propertyName, // the name of the property (must be a float)
  float value          // the value to be inserted for this property (must be a float)
);
```

If the property is not of the float type, an error will occur.

---


```zenscript
Spell setInteger(
  string propertyName, // the name of the property (must be a integer)
  int value            // the value to be inserted for this property (must be a integer)
);
```

If the property is not of the integer type, an error will occur.

---


```zenscript
Spell setString(
  string propertyName, // the name of the property (must be a string)
  string value         // the value to be inserted for this property (must be a string)
);
```

If the property is not of the string type, an error will occur.

---


```zenscript
Spell setCooldown(
  int value // the new spell cooldown (as an integer in ticks)
);
```


---


```zenscript
Spell setDamage(
  float value // the new damage of the spell
);
```

If the spell does not have a damage property, an error will occur. Consult /roots spells.

---


```zenscript
Spell setCost(
  Herb herb,    // the static herb reference found in Herbs (must be an existing cost)
  double amount // the double value of the new cost for this herb
);
```

This cannot be used to add a new cost to a spell, only to modify an existing cost.

---


```zenscript
Spell setModifierCost(
  Cost cost,    // the static cost type found in Costs (must be an existing cost)
  Herb herb,    // the static herb reference found in Herbs (must be an existing modifier cost)
  double amount // the double value of the new cost for this combination of cost type and herb
);
```

This cannot be used to add a new cost to a spell's modifier, only to modify an existing cost.

---


