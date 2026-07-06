package com.example.cactusrepair.mixin;

import java.util.List;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

	@Unique
	private static ItemStack cactusrepair$lastChecked = ItemStack.EMPTY;

	/**
	 * Пиглины принимают золотые самородки как предмет для бартера.
	 * Заодно запоминаем, какой предмет проверялся последним —
	 * именно он будет обмениваться в getBarteredItem.
	 */
	@Inject(
			method = "acceptsForBarter(Lnet/minecraft/item/ItemStack;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void cactusrepair$nuggetBarter(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		cactusrepair$lastChecked = stack;
		if (stack.isOf(Items.GOLD_NUGGET)) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * Если бартер шёл за самородки — всегда выдаём алмазы,
	 * по одному за каждый самородок в стаке.
	 */
	@Inject(method = "getBarteredItem", at = @At("HEAD"), cancellable = true)
	private static void cactusrepair$diamondsForNuggets(CallbackInfoReturnable<List<ItemStack>> cir) {
		if (cactusrepair$lastChecked.isOf(Items.GOLD_NUGGET)) {
			cir.setReturnValue(List.of(new ItemStack(Items.DIAMOND, cactusrepair$lastChecked.getCount())));
		}
	}
}
