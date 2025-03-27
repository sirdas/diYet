package me.sirdas.diyet;

import android.animation.LayoutTransition;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


public class RecipeAdapter extends ArrayAdapter<Basket> {
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener ocl) {
        onClickListener = ocl;
    }

    public RecipeAdapter(@NonNull Context context, ArrayList<Basket> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Basket recipe = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe, parent, false);
        }
        TextView tvRecipeName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
        TextView tvRecipeKcal = (TextView) convertView.findViewById(R.id.tv_recipe_kcal);
        final TextView tvRecipeFoods = (TextView) convertView.findViewById(R.id.tv_recipe_foods);
        ImageButton ibRecipeCheckout = (ImageButton) convertView.findViewById(R.id.ib_recipe_checkout);
        final Button bRecipeAdd = (Button) convertView.findViewById(R.id.b_recipe_add);
        final Button bRecipeDelete = (Button) convertView.findViewById(R.id.b_recipe_delete);
        DecimalFormat df1 = new DecimalFormat(".#");
        //Log.d("recipe", recipe.toSpannableString().toString());
        tvRecipeName.setText(recipe.getName());
        tvRecipeKcal.setText("·  " + df1.format(recipe.getKcal()) + " kcal");
        tvRecipeFoods.setText(recipe.toSpannableString());
        ibRecipeCheckout.setTag(recipe);
        ibRecipeCheckout.setOnClickListener(onClickListener);
        bRecipeAdd.setTag(recipe);
        bRecipeAdd.setOnClickListener(onClickListener);
        bRecipeDelete.setTag(recipe);
        bRecipeDelete.setOnClickListener(onClickListener);
        final ConstraintLayout clRecipe = convertView.findViewById(R.id.cl_recipe);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TransitionManager.beginDelayedTransition(clRecipe);
                if (tvRecipeFoods.getVisibility() == View.GONE) {
                    tvRecipeFoods.setVisibility(View.VISIBLE);
                    bRecipeAdd.setVisibility(View.VISIBLE);
                    bRecipeDelete.setVisibility(View.VISIBLE);
                } else {
                    tvRecipeFoods.setVisibility(View.GONE);
                    bRecipeAdd.setVisibility(View.GONE);
                    bRecipeDelete.setVisibility(View.GONE);
                }
                //TransitionManager.beginDelayedTransition(clRecipe);
            }
        });
        return convertView;
    }
}
