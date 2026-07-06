package com.example.cactusrepair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

public class CactusRepairMod implements ModInitializer {

	@Override
	public void onInitialize() {
		// Удар голема голой рукой -> выпадает 1 железный слиток за каждый удар
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damageTaken, blocked) -> {
			if (blocked) {
				return;
			}
			if (entity instanceof IronGolemEntity golem
					&& source.getAttacker() instanceof PlayerEntity player
					&& player.getMainHandStack().isEmpty()
					&& golem.getWorld() instanceof ServerWorld serverWorld) {
				golem.dropItem(serverWorld, Items.IRON_INGOT);
			}
		});
	}
}
