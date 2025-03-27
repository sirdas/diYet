package me.sirdas.diyet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class FoodDetailPagerAdapter extends PagerAdapter {

    private Context context;
    private Food food;

    public FoodDetailPagerAdapter(Context context, Food food) {
        this.context = context;
        this.food = food;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_food_detail, collection, false);
        TextView tvCarbValue = (TextView) layout.findViewById(R.id.tv_carb_value);
        TextView tvProteinValue = (TextView) layout.findViewById(R.id.tv_protein_value);
        TextView tvFatValue = (TextView) layout.findViewById(R.id.tv_fat_value);
        TextView tvCarbUnit = (TextView) layout.findViewById(R.id.tv_carb_unit);
        TextView tvProteinUnit = (TextView) layout.findViewById(R.id.tv_protein_unit);
        TextView tvFatUnit = (TextView) layout.findViewById(R.id.tv_fat_unit);
        DecimalFormat df1 = new DecimalFormat(".#");
        //DecimalFormat df2 = new DecimalFormat(".##");
        switch (position) {
            case 0:
                tvCarbValue.setText(df1.format(food.getCarbKcal()));
                tvProteinValue.setText(df1.format(food.getProteinKcal()));
                tvFatValue.setText(df1.format(food.getFatKcal()));
                tvCarbUnit.setText("kcal");
                tvProteinUnit.setText("kcal");
                tvFatUnit.setText("kcal");
                break;
            case 1:
                tvCarbValue.setText(df1.format(food.getCarbPerc()));
                tvProteinValue.setText(df1.format(food.getProteinPerc()));
                tvFatValue.setText(df1.format(food.getFatPerc()));
                tvCarbUnit.setText("%");
                tvProteinUnit.setText("%");
                tvFatUnit.setText("%");
                break;
            case 2:
                tvCarbValue.setText(df1.format(food.getCarbGrams()));
                tvProteinValue.setText(df1.format(food.getProteinGrams()));
                tvFatValue.setText(df1.format(food.getFatGrams()));
                tvCarbUnit.setText("g");
                tvProteinUnit.setText("g");
                tvFatUnit.setText("g");
                break;
        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
