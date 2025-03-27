package me.sirdas.diyet;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;
import java.util.HashSet;

public class RecipesFragment extends Fragment {
    protected ArrayList<Basket> savedRecipes;
    protected RecipeAdapter ra;
    protected ListView lvRecipes;
    protected HashSet<String> savedRecipeNames;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Basket recipe = (Basket) view.getTag();
            switch(view.getId()) {
                case R.id.ib_recipe_checkout: {
                    Basket recipeCopy = new Basket((Basket) view.getTag()); //deep copy
                    MainActivity ma = (MainActivity) getActivity();
                    TodayFragment tf = (TodayFragment) ma.tf;
                    recipeCopy.setAddedDate();
                    tf.addBasket(recipeCopy);
                    ma.bnwBottom.setSelectedItemId(R.id.dest_today);
                    break;
                }
                case R.id.cl_recipe: {

                    break;
                }
                case R.id.b_recipe_add: {
                    Basket recipeCopy = new Basket((Basket) view.getTag()); //deep copy
                    MainActivity ma = (MainActivity) getActivity();
                    ma.addRecipe(recipeCopy);
                    break;
                }
                case R.id.b_recipe_delete: {
                    MainActivity ma = (MainActivity) getActivity();
                    for (Basket dayBasket : (((TodayFragment)ma.tf).currentDay.getBaskets())) {
                        if (dayBasket.getName().equals(recipe.getName())) {
                            dayBasket.setSaved(false);
                            dayBasket.setName("Basket");
                            ((TodayFragment)ma.tf).da.notifyDataSetChanged();
                        }
                    }
                    savedRecipeNames.remove(recipe.getName());
                    savedRecipes.remove(recipe);
                    ra.notifyDataSetChanged();
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipes, container, false);
        ((MainActivity)getActivity()).bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        ra = new RecipeAdapter(getContext(), savedRecipes);
        lvRecipes = (ListView) v.findViewById(R.id.lv_recipes);
        lvRecipes.setAdapter(ra);
        ra.setOnItemClickListener(onClickListener);
        return v;
    }
}
