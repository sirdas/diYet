package me.sirdas.diyet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BasketDetailPagerAdapter extends PagerAdapter {

    private Context context;
    private Basket basket;

    public BasketDetailPagerAdapter(Context context, Basket basket) {
        this.context = context;
        this.basket = basket;
    }


    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_basket_detail, collection, false);
        TextView tvCarbValue = (TextView) layout.findViewById(R.id.tv_carb_value);
        TextView tvProteinValue = (TextView) layout.findViewById(R.id.tv_protein_value);
        TextView tvFatValue = (TextView) layout.findViewById(R.id.tv_fat_value);
        DecimalFormat df1 = new DecimalFormat(".#");
        switch (position) {
            case 0:
                tvCarbValue.setText(df1.format(basket.getCarbKcal()) + " kcal");
                tvProteinValue.setText(df1.format(basket.getProteinKcal()) + " kcal");
                tvFatValue.setText(df1.format(basket.getFatKcal()) + " kcal");
                break;
            case 1:
                tvCarbValue.setText(df1.format(basket.getCarbPerc()) + " %");
                tvProteinValue.setText(df1.format(basket.getProteinPerc()) + " %");
                tvFatValue.setText(df1.format(basket.getFatPerc()) + " %");
                break;
            case 2:
                tvCarbValue.setText(df1.format(basket.getCarbGrams()) + " g");
                tvProteinValue.setText(df1.format(basket.getProteinGrams()) + " g");
                tvFatValue.setText(df1.format(basket.getFatGrams()) + " g");
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
