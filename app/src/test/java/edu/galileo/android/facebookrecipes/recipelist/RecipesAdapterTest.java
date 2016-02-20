package edu.galileo.android.facebookrecipes.recipelist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.lib.ImageLoader;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.facebookrecipes.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by ykro.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowRecyclerViewAdapter.class})
public class RecipesAdapterTest extends BaseRecipeListTest {
    @Mock
    List<Recipe> recipes;
    @Mock
    ImageLoader imageLoader;
    @Mock
    OnItemClickListener onItemClickListener;

    RecyclerView recyclerView;
    List<Recipe> recipeList = Arrays.asList(new Recipe[]{
            new Recipe(),
            new Recipe(),
            new Recipe()});

    @Override
    public void setUp() throws Exception {
        super.setUp();
        for (Recipe recipe : recipeList) {
            recipe.setSourceURL("http://google.com");
        }

        recyclerView = new RecyclerView(RuntimeEnvironment.application);
        recyclerView.setLayoutManager(new LinearLayoutManager(RuntimeEnvironment.application));

        RecipesAdapter adapterPopulated = new RecipesAdapter(recipeList, imageLoader, onItemClickListener);
        recyclerView.setAdapter(adapterPopulated);
    }

    @Test
    public void setRecipes_itemCountMatch() {
        RecipesAdapter adapter = new RecipesAdapter(recipeList, imageLoader, onItemClickListener);

        adapter.setRecipes(recipeList);

        assertEquals(adapter.getItemCount(), recipeList.size());
    }

    @Test
    public void removeRecipe_isRemovingFromAdapter() {
        RecipesAdapter adapter = new RecipesAdapter(recipes, imageLoader, onItemClickListener);
        Recipe recipe = new Recipe();
        adapter.removeRecipe(recipe);
        verify(recipes).remove(recipe);
    }

    @Test
    public void onItemClick_listenerCalled() {
        int positionToClick = 0;
        ShadowRecyclerViewAdapter shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(recyclerView.getAdapter());

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClick(positionToClick);

        verify(onItemClickListener).onItemClick(recipeList.get(positionToClick));
    }

    @Test
    public void onFavoriteClick_listenerCalled() {
        int positionToClick = 0;
        ShadowRecyclerViewAdapter shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(recyclerView.getAdapter());

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgFav);

        verify(onItemClickListener).onFavClick(recipeList.get(positionToClick));
    }

    @Test
    public void onFavoriteClick_viewHolderImageChanged() {
        int positionToClick = 0;
        Recipe recipeToClick = recipeList.get(positionToClick);
        boolean favorite = true;
        recipeToClick.setFavorite(favorite);

        ShadowRecyclerViewAdapter shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(recyclerView.getAdapter());
        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgFav);

        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToClick);
        ImageButton imgFav = (ImageButton) viewHolder.itemView.findViewById(R.id.imgFav);
        boolean favoriteFromVH = (Boolean) imgFav.getTag();

        assertEquals(favorite,favoriteFromVH);
    }

    @Test
    public void onNonFavoriteClick_viewHolderImageChanged() {
        int positionToClick = 0;
        Recipe recipeToClick = recipeList.get(positionToClick);
        boolean favorite = false;
        recipeToClick.setFavorite(favorite);

        ShadowRecyclerViewAdapter shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(recyclerView.getAdapter());
        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgFav);

        RecipesAdapter.ViewHolder viewHolder = (RecipesAdapter.ViewHolder) shadowAdapter.getViewHolderForPosition(positionToClick);
        ImageButton imgFav = (ImageButton) viewHolder.itemView.findViewById(R.id.imgFav);
        boolean favoriteFromVH = (Boolean) imgFav.getTag();

        assertEquals(favorite,favoriteFromVH);
    }

    @Test
    public void onDeleteClick_listenerCalled() {
        int positionToClick = 0;
        ShadowRecyclerViewAdapter shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(recyclerView.getAdapter());

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performClickOverViewInViewHolder(positionToClick, R.id.imgDelete);

        verify(onItemClickListener).onDeleteClick(recipeList.get(positionToClick));
    }

}