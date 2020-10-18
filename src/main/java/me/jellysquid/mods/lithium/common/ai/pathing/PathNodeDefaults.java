package me.jellysquid.mods.lithium.common.ai.pathing;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;

public class PathNodeDefaults {
    public static PathNodeType getNeighborNodeType(BlockState state) {
        if (state.isAir()) {
            return PathNodeType.OPEN;
        }

        // [VanillaCopy] LandPathNodeMaker#getNodeTypeFromNeighbors
        // Determine what kind of obstacle type this neighbor is
        if (state.isOf(Blocks.CACTUS)) {
            return PathNodeType.DANGER_CACTUS;
        } else if (state.isOf(Blocks.SWEET_BERRY_BUSH)) {
            return PathNodeType.DANGER_OTHER;
        } else if (isFireDangerSource(state)) {
            return PathNodeType.DANGER_FIRE;
        } else if (state.getFluidState().isIn(FluidTags.WATER)) {
            return PathNodeType.WATER_BORDER;
        } else {
            return PathNodeType.OPEN;
        }
    }

    public static PathNodeType getNodeType(BlockState state) {
        if (state.isAir()) {
            return PathNodeType.OPEN;
        }

        Block block = state.getBlock();
        Material material = state.getMaterial();

        if (state.isIn(BlockTags.TRAPDOORS) || state.isOf(Blocks.LILY_PAD)) {
            return PathNodeType.TRAPDOOR;
        }

        if (state.isOf(Blocks.CACTUS)) {
            return PathNodeType.DAMAGE_CACTUS;
        }

        if (state.isOf(Blocks.SWEET_BERRY_BUSH)) {
            return PathNodeType.DAMAGE_OTHER;
        }

        if (state.isOf(Blocks.HONEY_BLOCK)) {
            return PathNodeType.STICKY_HONEY;
        }

        if (state.isOf(Blocks.COCOA)) {
            return PathNodeType.COCOA;
        }

        if (isFireDangerSource(state)) {
            return PathNodeType.DAMAGE_FIRE;
        }

        if (DoorBlock.isWoodenDoor(state) && !state.get(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_WOOD_CLOSED;
        }

        if ((block instanceof DoorBlock) && (material == Material.METAL) && !state.get(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_IRON_CLOSED;
        }

        if ((block instanceof DoorBlock) && state.get(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_OPEN;
        }

        if (block instanceof AbstractRailBlock) {
            return PathNodeType.RAIL;
        }

        if (block instanceof LeavesBlock) {
            return PathNodeType.LEAVES;
        }

        if (block.isIn(BlockTags.FENCES) || block.isIn(BlockTags.WALLS) || ((block instanceof FenceGateBlock) && !state.get(FenceGateBlock.OPEN))) {
            return PathNodeType.FENCE;
        }

        // Retrieve the fluid state from the block state to avoid a second lookup
        FluidState fluid = state.getFluidState();

        if (fluid.isIn(FluidTags.WATER)) {
            return PathNodeType.WATER;
        } else if (fluid.isIn(FluidTags.LAVA)) {
            return PathNodeType.LAVA;
        }

        return PathNodeType.OPEN;
    }

    private static boolean isFireDangerSource(BlockState blockState) {
        return blockState.isIn(BlockTags.FIRE) || blockState.isOf(Blocks.LAVA) || blockState.isOf(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(blockState);
    }
}
