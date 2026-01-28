package crom.sleepingbag.block;

import crom.sleepingbag.SleepingBag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagBlockEntity extends BlockEntity {
    private final DyeColor color;

    public SleepingBagBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SleepingBag.SLEEPING_BAG_BLOCK_ENTITY, blockPos, blockState);
        this.color = ((SleepingBagBlock) blockState.getBlock()).getColor();
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyeColor getColor() {
        return this.color;
    }
}
