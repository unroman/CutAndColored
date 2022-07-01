package einstein.cutandcolored.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import einstein.cutandcolored.CutAndColored;
import einstein.cutandcolored.init.ModBlocks;
import einstein.cutandcolored.util.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BlockLootTableGenerator extends BlockLoot {
	
	private List<Block> nonSlabBlocks = new ArrayList<Block>(CutAndColored.allBlocks.stream()
			.filter((block) -> !Util.getBlockRegistryName(block).getPath().contains("_slab"))
			.filter((block) -> !Util.getBlockRegistryName(block).getPath().contains("glass"))
			.collect(Collectors.toList()));
	
	private List<Block> slabBlocks = new ArrayList<Block>(CutAndColored.allBlocks.stream()
			.filter((block) -> Util.getBlockRegistryName(block).getPath().contains("_slab"))
			.filter((block) -> !Util.getBlockRegistryName(block).getPath().contains("glass"))
			.collect(Collectors.toList()));
	
	private List<Block> silkTouchBlocks = new ArrayList<Block>(CutAndColored.allBlocks.stream()
			.filter((block) -> Util.getBlockRegistryName(block).getPath().contains("glass") || Util.getBlockRegistryName(block).getPath().contains("window"))
			.filter((block) -> !Util.getBlockRegistryName(block).getPath().contains("_slab"))
			.filter((block) -> !ModBlocks.GLASSCUTTER.get().equals(block))
			.filter((block) -> !ModBlocks.HORIZONTAL_SOUL_GLASS_PANE.get().equals(block))
			.filter((block) -> !ModBlocks.TINTED_GLASS_PANE.get().equals(block))
			.filter((block) -> !ModBlocks.TINTED_GLASS_STAIRS.get().equals(block))
			.filter((block) -> !ModBlocks.TINTED_GLASS_SLAB.get().equals(block))
			.filter((block) -> !ModBlocks.TINTED_GLASS_WINDOW.get().equals(block))
			.filter((block) -> !ModBlocks.TINTED_GLASS_WINDOW_PANE.get().equals(block))
			.collect(Collectors.toList()));
	
	private List<Block> glassSlabBlocks = new ArrayList<Block>(CutAndColored.allBlocks.stream()
			.filter((block) -> Util.getBlockRegistryName(block).getPath().contains("glass"))
			.filter((block) -> Util.getBlockRegistryName(block).getPath().contains("_slab"))
			.collect(Collectors.toList()));
	
	@Override
	protected void addTables() {
		for (int i = 0; i < nonSlabBlocks.size(); i++) {
			dropSelf(nonSlabBlocks.get(i));
		}
		for (int i = 0; i < slabBlocks.size(); i++) {
			add(slabBlocks.get(i), BlockLoot::createSlabItemTable);
		}
		for (int i = 0; i < silkTouchBlocks.size(); i++) {
			dropWhenSilkTouch(silkTouchBlocks.get(i));
		}
		for (int i = 0; i < glassSlabBlocks.size(); i++) {
			add(glassSlabBlocks.get(i), BlockLootTableGenerator::createSilkTouchSlabTable);
		}
		dropSelf(ModBlocks.GLASSCUTTER.get());
		dropSelf(ModBlocks.TINTED_GLASS_PANE.get());
		dropSelf(ModBlocks.TINTED_GLASS_STAIRS.get());
		dropSelf(ModBlocks.TINTED_GLASS_WINDOW.get());
		dropSelf(ModBlocks.TINTED_GLASS_WINDOW_PANE.get());
		add(ModBlocks.TINTED_GLASS_SLAB.get(), BlockLoot::createSlabItemTable);
	}
	
	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks() {
		List<Block> list = new ArrayList<Block>(CutAndColored.allBlocks);
		list.remove(list.indexOf(ModBlocks.HORIZONTAL_SOUL_GLASS_PANE.get()));
		return list;
	}
	
	protected static LootTable.Builder createSilkTouchSlabTable(Block slab) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1))
				.add(applyExplosionDecay(slab, LootItem.lootTableItem(slab)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
				.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab).setProperties(StatePropertiesPredicate.Builder
				.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
	}
}
