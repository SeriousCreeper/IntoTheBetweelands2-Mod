
### Class

```zenscript
import mods.roots.Bark;
```

#### Methods

```zenscript
void addRecipe(
  string name,        // the name of the recipe
  IItemStack woodLog, // the itemstack equivalent of the wood log being broken
  IItemStack bark     // the itemstack of the type of bark this log produces (including stack count)
);
```

Adds a recipe that converts a block (in the form of an item stack) into another item stack. Log input *must* be an itemblock.

---


```zenscript
void removeRecipe(
  IItemStack bark // the itemstack of the type of bark to remove (excluding stack size)
);
```

Removes a bark recipe via output from the list.

---


