package crom.sleepingbag;

import crom.sleepingbag.block.SleepingBagBlock;
import crom.sleepingbag.block.SleepingBagBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class SleepingBag implements ModInitializer {
	public static final String MOD_ID = "sleepingbag";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Maps to store all colored sleeping bags
	public static final Map<DyeColor, Block> SLEEPING_BAGS = new EnumMap<>(DyeColor.class);
	public static final Map<DyeColor, Item> SLEEPING_BAG_ITEMS = new EnumMap<>(DyeColor.class);

	// Block Entity Type - will be initialized after blocks
	public static BlockEntityType<SleepingBagBlockEntity> SLEEPING_BAG_BLOCK_ENTITY;

	// Static initialization of blocks
	static {
		for (DyeColor color : DyeColor.values()) {
			ResourceKey<Block> blockKey = ResourceKey.create(
					Registries.BLOCK,
					Identifier.fromNamespaceAndPath(MOD_ID, color.getName() + "_sleeping_bag")
			);

			Block block = new SleepingBagBlock(
					color,
					BlockBehaviour.Properties.of()
							.mapColor(blockState -> blockState.getValue(SleepingBagBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WOOL)
							.sound(SoundType.WOOD)
							.strength(0.2F)
							.noOcclusion()
							.ignitedByLava()
							.pushReaction(PushReaction.DESTROY)
							.setId(blockKey)
			);

			SLEEPING_BAGS.put(color, block);
		}

		// Create block entity type with all sleeping bag blocks
		SLEEPING_BAG_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
				SleepingBagBlockEntity::new,
				SLEEPING_BAGS.values().toArray(new Block[0])
		).build();

		// Create items
		for (DyeColor color : DyeColor.values()) {
			ResourceKey<Item> itemKey = ResourceKey.create(
					Registries.ITEM,
					Identifier.fromNamespaceAndPath(MOD_ID, color.getName() + "_sleeping_bag")
			);

			Item item = new BlockItem(
					SLEEPING_BAGS.get(color),
					new Item.Properties().stacksTo(1).useBlockDescriptionPrefix().setId(itemKey)
			);

			SLEEPING_BAG_ITEMS.put(color, item);
		}
	}

	@Override
	public void onInitialize() {
		// Register all blocks
		for (DyeColor color : DyeColor.values()) {
			Registry.register(
					BuiltInRegistries.BLOCK,
					Identifier.fromNamespaceAndPath(MOD_ID, color.getName() + "_sleeping_bag"),
					SLEEPING_BAGS.get(color)
			);
		}

		// Register block entity type
		Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(MOD_ID, "sleeping_bag"),
				SLEEPING_BAG_BLOCK_ENTITY
		);

		// Register all items
		for (DyeColor color : DyeColor.values()) {
			Registry.register(
					BuiltInRegistries.ITEM,
					Identifier.fromNamespaceAndPath(MOD_ID, color.getName() + "_sleeping_bag"),
					SLEEPING_BAG_ITEMS.get(color)
			);
		}

		// Add to creative tab
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			for (DyeColor color : DyeColor.values()) {
				content.accept(SLEEPING_BAG_ITEMS.get(color));
			}
		});

		LOGGER.info("Sleeping Bag mod initialized!");
	}

	public static Block getSleepingBag(DyeColor color) {
		return SLEEPING_BAGS.get(color);
	}
}
