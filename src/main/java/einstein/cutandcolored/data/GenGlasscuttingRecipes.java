package einstein.cutandcolored.data;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;

public class GenGlasscuttingRecipes extends RecipeResources {

	public GenGlasscuttingRecipes(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		glasscuttingRecipe(consumer, "test_recipe", Items.POTATO, Items.BAKED_POTATO);
	}
}
