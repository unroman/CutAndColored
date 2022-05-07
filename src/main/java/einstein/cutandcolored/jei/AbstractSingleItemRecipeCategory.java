package einstein.cutandcolored.jei;

import einstein.cutandcolored.item.crafting.AbstractSingleItemRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSingleItemRecipeCategory<T extends AbstractSingleItemRecipe> implements IRecipeCategory<T> {

	public static final int width = 82;
	public static final int height = 34;
	private final IDrawable background;
	private final IDrawable icon;
	private final Component title;
	
	public AbstractSingleItemRecipeCategory(String localizedName, ItemStack iconStack, IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 220, width, height);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, iconStack);
		title = new TranslatableComponent(localizedName);
	}
	
	@Override
	public ResourceLocation getUid() {
		return getRecipeType().getUid();
	}
	
	@Override
	public Class<? extends T> getRecipeClass() {
		return getRecipeType().getRecipeClass();
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	
	@Override
	public Component getTitle() {
		return title;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getResultItem());
	}
}
