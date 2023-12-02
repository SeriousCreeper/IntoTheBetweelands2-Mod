package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.world.data.SpellLibraryData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(value = SpellLibraryData.class, remap = false)
public class MixinSpellLibraryData {
    @Shadow
    private List<LibrarySpellInfo> list = null;

    @Shadow
    private Map<SpellBase, LibrarySpellInfo> spells = new HashMap();


    @Inject(method = "asList", at = @At("HEAD"), cancellable = true)
    private void injectAsList(CallbackInfoReturnable<List<LibrarySpellInfo>> cir) {
        if (this.list == null) {
            this.list = this.spells.values().stream().filter((o) -> {
                return o.getSpell() != null;
            }).filter(LibrarySpellInfo::isObtained).sorted(Comparator.comparing(a -> a.getSpell() == null ? "" : a.getSpell().getRegistryName().getPath())).collect(Collectors.toList());
        }

        this.list.sort(Comparator.comparing((a) -> {
            return a.getSpell() == null ? "" : a.getSpell().getRegistryName().getPath();
        }));

        cir.setReturnValue(this.list);
    }
}
