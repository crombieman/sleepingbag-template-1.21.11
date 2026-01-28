package crom.sleepingbag.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagBlock extends BedBlock {

    public SleepingBagBlock(DyeColor color, Properties properties) {
        super(color, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SleepingBagBlockEntity(pos, state);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        // Don't reduce fall damage like beds do - use default block behavior
        entity.causeFallDamage((float) fallDistance, 1.0F, level.damageSources().fall());
    }

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter blockGetter, Entity entity) {
        // Don't bounce - just stop vertical movement (default block behavior)
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }

}
