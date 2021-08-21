
### Class

```zenscript
import mods.roots.Fey;
```

#### Methods

```zenscript
void addRecipe(
  string name,         // the name of the recipe; if replacing an existing recipe, be sure to use the same name to ensure Patchouli continuity
  IItemStack output,   // the itemstack produced by this recipe
  IIngredient[] inputs // an array of IIngredients that make up the recipe; must contain 5 items
);
```

Creates a recipe producing output from an array of ingredients (transforms are supported), requires a name.

---


```zenscript
void addRecipe(
  string name,          // the name of the recipe; if replacing an existing recipe, be sure to use the same name to ensure Patchouli continuity
  IItemStack output,    // the itemstack produced by this recipe
  IIngredient[] inputs, // an array of IIngredients that make up the recipe; must contain 5 items
  int xp                // the amount of xp (in levels) to reward the player for crafting this recipe
);
```

Creates a recipe producing output from an array of ingredients (transforms are supported), requires a name. Additional drops the specified amount of experience whenever the recipe is crafted.

---


```zenscript
void removeRecipe(
  IItemStack output // the item produced by the recipe you wish to remove
);
```

Removes a Fey Crafting recipe via the output produced by the recipe.

---


