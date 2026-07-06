package com.example.cactusrepair.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

	/**
	 * Если предмет с прочностью получает урон от кактуса —
	 * вместо уничтожения полностью его чиним и отменяем урон.
	 */
	@Inject(
			method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private void cactusrepair$repairOnCactus(ServerWorld world, DamageSource source, float amount,
			CallbackInfoReturnable<Boolean> cir) {
		if (!source.isOf(DamageTypes.CACTUS)) {
			return;
		}
		ItemEntity self = (ItemEntity) (Object) this;
		ItemStack stack = self.getStack();
		if (stack.isDamageable()) {
			if (stack.getDamage() > 0) {
				stack.setDamage(0); // полный ремонт
			}
			cir.setReturnValue(false); // кактус не наносит урон предмету
		}
	}
}
