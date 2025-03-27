package me.sirdas.diyet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import androidx.annotation.NonNull;

public class DayAdapter extends ArrayAdapter<Basket> {
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener ocl) {
        onClickListener = ocl;
    }

    public DayAdapter(@NonNull Context context, ArrayList<Basket> baskets) {
        super(context, 0, baskets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Basket basket = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_basket, parent, false);
        }
        TextView tvBasketName = (TextView) convertView.findViewById(R.id.tv_day_basket_name);
        TextView tvBasketKcal = (TextView) convertView.findViewById(R.id.tv_day_basket_kcal);
        TextView tvBasketFoods = (TextView) convertView.findViewById(R.id.tv_day_basket_foods);
        TextView tvBasketTime = (TextView) convertView.findViewById(R.id.tv_day_basket_time);
        DecimalFormat df1 = new DecimalFormat(".#");
        tvBasketName.setText(basket.getName());
        tvBasketKcal.setText(df1.format(basket.getKcal()) + " kcal");
        tvBasketFoods.setText(basket.toSpannableString());
        Date addedDate = basket.getAddedDate();
        SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("kk:mm", Locale.getDefault());
        tvBasketTime.setText(simpDate.format(addedDate));
        ImageButton ibBasketDelete = (ImageButton) convertView.findViewById(R.id.ib_day_basket_delete);
        ImageButton ibBasketSave = (ImageButton) convertView.findViewById(R.id.ib_day_basket_save);
        ibBasketDelete.setTag(basket);
        ibBasketDelete.setOnClickListener(onClickListener);
        if (basket.isSaved()) {
            ibBasketSave.setEnabled(false);
        } else {
            ibBasketSave.setEnabled(true);
            ibBasketSave.setTag(basket);
            ibBasketSave.setOnClickListener(onClickListener);
        }
        return convertView;
    }
}
