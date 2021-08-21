
### Class

```zenscript
import mods.roots.predicates.PropertyPredicate;
```

#### Methods

```zenscript
PropertyPredicate create(
  IBlockState state, // description of a simple blockstate
  string properties  // a string containing the property name that must match
);
```

Creates an IPredicate where the specified state is compared against other states, where the block type must match and the values of the specified property names must match.

---


```zenscript
PropertyPredicate create(
  IBlockState state,  // description of a simple blockstate
  string[] properties // an array of strings containing property names that must match
);
```

Creates an IPredicate where the specified state is compared against other states, where the block type must match and the values of all of the specified property names must match.

---


