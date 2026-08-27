package hsm.hotshirtlessmen.mixin;

import hsm.hotshirtlessmen.HSM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private ItemStack destroyingItem;
    @Final @Shadow private Minecraft minecraft;
    @Shadow private BlockPos destroyBlockPos;

    @Inject(method = "sameDestroyTarget", at = @At("RETURN"), cancellable = true)
    private void itemStackCompare(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() || !pos.equals(destroyBlockPos)) return;
        assert minecraft.player != null;
        ItemStack hand = minecraft.player.getMainHandItem();
        if (HSM.compareComponents(hand.getComponents(), destroyingItem.getComponents())) {
            destroyingItem = hand;
            cir.setReturnValue(true);
        }
    }
}