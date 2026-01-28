package crom.sleepingbag.mixin;

import crom.sleepingbag.block.SleepingBagBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void sleepingbag$preventSpawnPointSet(@Nullable ServerPlayer.RespawnConfig respawnConfig, boolean sendMessage, CallbackInfo ci) {
        if (respawnConfig != null) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            BlockPos pos = respawnConfig.respawnData().pos();
            BlockState state = player.level().getBlockState(pos);

            if (state.getBlock() instanceof SleepingBagBlock) {
                // Cancel the spawn point setting for sleeping bags
                ci.cancel();
            }
        }
    }
}
