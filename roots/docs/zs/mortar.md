
### Class

```zenscript
import mods.roots.Mortar;
```

#### Methods

```zenscript
void addRecipe(
  string name,         // the name of this recipe, should match a recipe being replaced
  IItemStack output,   // the item output of this recipe
  IIngredient[] inputs // an array of ingredients that is either 5 long or 1 long
);
```

Creates a recipe to create output from an array of ingredients (allows transformations). If the array is 5 long, a single recipe will be produced. If the array consists of only one ingredient, 5 separate recipes will be produced, with the output adjusted every time to compensate.

---


```zenscript
void changeSpell(
  string spellName,    // the name of the spell as in the spell registry
  IIngredient[] inputs // an array of 5 items that are the new ingredients for the recipe
);
```

Allows the modification of the recipe for a Spell using the specified array of 5 ingredients (allows for transformations).

---


```zenscript
void removeRecipe(
  IItemStack output // the item stack produced by the recipe
);
```

Removes a Mortar Recipe based on output. Compares output to existing recipes without regard for size, meaning that matching recipes with 1-5 inputs and 1-5x outputs will all be removed.

---


