
### Class

```zenscript
import mods.roots.Pyre;
```

#### Methods

```zenscript
void addRecipe(
  string name,         // the name of the recipe being added; if replacing an existing game recipe, ensure the correct name is used
  IItemStack output,   // the output of this recipe
  IIngredient[] inputs // a list of five ingredients (no more, no less)
);
```

Adds a Pyre crafting recipe that produces output after the standard amount of time, with the specified input ingredients (with potential transformers).

---


```zenscript
void addRecipe(
  string name,          // the name of the recipe being added; if replacing an existing game recipe, ensure the correct name is used
  IItemStack output,    // the output of this recipe
  IIngredient[] inputs, // a list of five ingredients
  int xp                // the amount of xp in levels that is granted after crafting
);
```

Adds a Pyre crafting recipe that produces output after the standard amount of time, with the specified input ingredients (with potential transformers). Allows for the specification of an amount of experience to be generated once the craft is finished.

---


```zenscript
void removeRecipe(
  IItemStack output // the output of the recipe to remove
);
```

Removes a Pyre crafting recipe based on its output.

---


