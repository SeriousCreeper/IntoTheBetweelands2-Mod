
### Class

```zenscript
import mods.roots.Transmutation;
```

#### Methods

```zenscript
void removeRecipe(
  string name // the name of the recipe being removed
);
```

Specifically removes a Transmutation Recipe based on the name of that recipe.

---


```zenscript
void addStateToStateRecipe(
  string name,              // the name of the recipe being created
  IPredicate start,         // the predicate describing the starting state being converted
  IBlockState result,       // the block state to convert to
  IWorldPredicate condition // the condition of this transition (can be null)
);
```

Add a Transmutation Recipe that converts from one IPredicate state-predicate into a block state, if the IWorldPredicate condition (which can be null, meaning that it will always match) is true.

---


```zenscript
void addStateToItemRecipe(
  string name,              // the name of the recipe being created
  IPredicate start,         // the predicate describing the starting state being converted
  IItemStack result,        // the item stack to convert to
  IWorldPredicate condition // the condition of this transition (can be null)
);
```

Add a Transmutation Recipe that converts from one IPredicate state-predicate into an item, if the IWorldPredicate condition (which can be null, meaning that it will always match) is true.

---


