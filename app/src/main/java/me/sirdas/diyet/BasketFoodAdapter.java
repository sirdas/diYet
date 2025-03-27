package me.sirdas.diyet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import androidx.annotation.NonNull;

public class BasketFoodAdapter extends ArrayAdapter<Food> {
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener ocl) {
        onClickListener = ocl;
    }

    public BasketFoodAdapter(@NonNull Context context, ArrayList<Food> foods) {
        super(context, 0, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food food = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_basket_food, parent, false);
        }
        TextView tvBasketFoodName = (TextView) convertView.findViewById(R.id.tv_basket_food_name);
        TextView tvBasketFoodWeight = (TextView) convertView.findViewById(R.id.tv_basket_food_weight);
        TextView tvBasketFoodKcal = (TextView) convertView.findViewById(R.id.tv_basket_food_kcal);
        DecimalFormat df1 = new DecimalFormat(".#");
        tvBasketFoodName.setText(food.getName());
        tvBasketFoodWeight.setText(df1.format(food.getQuantity() * food.getUnit()) + " g");
        tvBasketFoodKcal.setText(df1.format(food.getKcal()));
        ImageButton ibBasketFoodDelete = (ImageButton) convertView.findViewById(R.id.ib_basket_food_delete);
        ibBasketFoodDelete.setTag(food);
        ibBasketFoodDelete.setOnClickListener(onClickListener);
        convertView.setTag(food);
        convertView.setOnClickListener(onClickListener);
        return convertView;
    }

}
