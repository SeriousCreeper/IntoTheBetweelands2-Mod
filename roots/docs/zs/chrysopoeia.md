
### Class

```zenscript
import mods.roots.Chrysopoeia;
```

#### Methods

```zenscript
void addRecipe(
  string name,            // the name of the recipe being added
  IIngredient ingredient, // a single ingredient (may have variable stack size)
  IItemStack output       // the output produce by Transubstantiation
);
```

Adds a transmutative recipe that converts an input (in the form of an ingredient, possibly with a variable stack size, transforms are supported), into an output (as an itemstack). Requires a name.

---


```zenscript
void removeRecipeByOutput(
  IItemStack output // the output of the recipe you wish to remove
);
```

Removes a transmutative recipe based on the output of the recipe, matches regardless of stack sizes.

---


```zenscript
void removeRecipe(
  IItemStack input // the exact input of the recipe you wish to remove
);
```

Removes a transmutative recipe based on the exact input (including size, tag, etc)

---


