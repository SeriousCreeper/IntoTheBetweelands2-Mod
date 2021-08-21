
### Class

```zenscript
import mods.roots.Rituals;
```

#### Methods

```zenscript
void modifyRitual(
  string name,         // the name of the ritual whose ingredients you wish to modify
  IIngredient[] inputs // a list of five ingredients (no more, no less)
);
```

Modifies the specified Ritual recipe to use the five ingredients specified.

---


```zenscript
Ritual getRitual(
  string ritualName // the name of the ritual to be fetched; will prepend `ritual_` if it doesn't start with `ritual_`.
);
```

Returns a Ritual object which can have its properties modified.

---


