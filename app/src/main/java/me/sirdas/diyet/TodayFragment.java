package me.sirdas.diyet;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;

public class TodayFragment extends Fragment implements EditLimitDialogFragment.EditLimitDialogListener {

    protected ConstraintLayout clDayBar;
    protected TextView tvDayKcal;
    protected int barWidth;
    protected Day currentDay;
    protected DayAdapter da;
    protected ListView lvDay;
    protected int kcalLimit;
    protected View vBarCarb;
    protected View vBarProtein;
    protected View vBarFat;
    protected TextView tvBarCarb;
    protected TextView tvBarProtein;
    protected TextView tvBarFat;
    protected boolean showingKcal = true;
    protected TextView tvDayEmpty;
    //protected int[] asts = {1, 12, 14, 16, 18};
    protected boolean didSetLimit;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Basket basket = (Basket) view.getTag();
            switch(view.getId()) {
                case R.id.ib_day_basket_delete:
                    deleteBasket(basket);
                    break;
                case R.id.cl_day_basket:
                    break;
                case R.id.include_day:
                    switchBarText();
                    break;
                case R.id.ib_day_basket_save:
                    ((MainActivity)getActivity()).showEditRecipeNameDialog(basket);
                    break;
                case R.id.tv_day_kcal:
                    showEditLimitDialog(didSetLimit);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        vBarCarb = v.findViewById(R.id.v_bar_carb);
        vBarProtein = v.findViewById(R.id.v_bar_protein);
        vBarFat = v.findViewById(R.id.v_bar_fat);
        tvBarCarb = v.findViewById(R.id.tv_bar_carb);
        tvBarProtein = v.findViewById(R.id.tv_bar_protein);
        tvBarFat = v.findViewById(R.id.tv_bar_fat);
        tvDayKcal = v.findViewById(R.id.tv_day_kcal);
        tvDayEmpty = v.findViewById(R.id.tv_day_empty);
        clDayBar = (ConstraintLayout) v.findViewById(R.id.include_day);
        if (!didSetLimit) {
            showEditLimitDialog(false);
        }
        clDayBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                barWidth = clDayBar.getWidth(); //width is ready
                updateKcalString();
                updateBar();
                clDayBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        da = new DayAdapter(getContext(), currentDay.getBaskets());
        lvDay = (ListView) v.findViewById(R.id.lv_day);
        lvDay.setAdapter(da);
        da.setOnItemClickListener(onClickListener);
        tvDayKcal.setOnClickListener(onClickListener);
        clDayBar.setOnClickListener(onClickListener);
        clDayBar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        clDayBar.getLayoutTransition().disableTransitionType(LayoutTransition.CHANGE_APPEARING);
        clDayBar.getLayoutTransition().disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        clDayBar.getLayoutTransition().disableTransitionType(LayoutTransition.APPEARING); //APPEARING and DISAPPEARING cause problems in animation
        clDayBar.getLayoutTransition().disableTransitionType(LayoutTransition.DISAPPEARING);
        if (currentDay.getBaskets().isEmpty()) {
            tvDayEmpty.setVisibility(View.VISIBLE);
        }
        return v;
    }
    protected void addBasket(Basket basket) {
        currentDay.add(basket);
        da.notifyDataSetChanged();
        updateKcalString();
        updateBar();
        if (tvDayEmpty.getVisibility() == View.VISIBLE) {
            tvDayEmpty.setVisibility(View.GONE);
        }
    }

    protected void deleteBasket(Basket basket) {
        if (currentDay.delete(basket)) {
            da.notifyDataSetChanged();
            updateKcalString();
            updateBar();
            if (currentDay.getBaskets().isEmpty()) {
                tvDayEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void updateBar() {
        double carbKcal = currentDay.getCarbKcal();
        double proteinKcal = currentDay.getProteinKcal();
        double fatKcal = currentDay.getFatKcal();

        //animation?
        int totalKcal = (int)Math.round(currentDay.getKcal());
        if (totalKcal > 0) {
            int kcalWidth = Math.round(totalKcal * barWidth / kcalLimit);
            if (kcalWidth > barWidth) {
                kcalWidth = barWidth;
            }
            Log.d("bar", "update");
            int carbWidth = (int)(carbKcal * kcalWidth / totalKcal);
            int proteinWidth = (int)(proteinKcal * kcalWidth / totalKcal);
            int fatWidth = (int)(fatKcal * kcalWidth / totalKcal);
            int remainderWidth = kcalWidth - (carbWidth + proteinWidth + fatWidth);
            int maxWidth = Math.max(Math.max(carbWidth, proteinWidth), fatWidth);
            if (maxWidth == carbWidth) {
                carbWidth += remainderWidth;
            } else if (maxWidth == proteinWidth) {
                proteinWidth += remainderWidth;
            } else {
                fatWidth += remainderWidth;
            }
            vBarCarb.getLayoutParams().width = carbWidth;
            vBarProtein.getLayoutParams().width = proteinWidth;
            vBarFat.getLayoutParams().width = fatWidth;
        } else {
            vBarCarb.getLayoutParams().width = 0;
            vBarProtein.getLayoutParams().width = 0;
            vBarFat.getLayoutParams().width = 0;
        }
        vBarCarb.requestLayout();
        vBarProtein.requestLayout();
        vBarFat.requestLayout();
        viewBarText(showingKcal);
    }

    private void viewBarText(boolean showKcal) {
        DecimalFormat df1 = new DecimalFormat(".#");
        if (showKcal) {
            tvBarCarb.setText(df1.format(currentDay.getCarbKcal()));
            tvBarProtein.setText(df1.format(currentDay.getProteinKcal()));
            tvBarFat.setText(df1.format(currentDay.getFatKcal()));
        } else {
            tvBarCarb.setText(df1.format(currentDay.getCarbPerc()) + "%");
            tvBarProtein.setText(df1.format(currentDay.getProteinPerc()) + "%");
            tvBarFat.setText(df1.format(currentDay.getFatPerc()) + "%");
        }
        tvBarCarb.measure(0, 0);
        tvBarProtein.measure(0, 0);
        tvBarFat.measure(0, 0);
        if (vBarCarb.getLayoutParams().width < tvBarCarb.getMeasuredWidth()) {
            tvBarCarb.setVisibility(View.GONE); // BUG: percent sign is wider than digits so the percent text may be set to invisible while the kcal text would still be shown
        } else {
            tvBarCarb.setVisibility(View.VISIBLE);
        }
        if (vBarProtein.getLayoutParams().width < tvBarProtein.getMeasuredWidth()) {
            tvBarProtein.setVisibility(View.GONE);
        } else {
            tvBarProtein.setVisibility(View.VISIBLE);
        }
        if (vBarFat.getLayoutParams().width < tvBarFat.getMeasuredWidth()) {
            tvBarFat.setVisibility(View.GONE);
        } else {
            tvBarFat.setVisibility(View.VISIBLE);
        }

    }

    private void switchBarText() {
        if (showingKcal) {
            viewBarText(false);
            showingKcal = false;
        } else {
            viewBarText(true);
            showingKcal = true;
        }
    }

    private void showEditLimitDialog(boolean didSetLimit) {
        FragmentManager fm = getFragmentManager();
        EditLimitDialogFragment editLimitDialogFragment = EditLimitDialogFragment.newInstance(didSetLimit);
        editLimitDialogFragment.setTargetFragment(TodayFragment.this, 300);
        editLimitDialogFragment.show(fm, "fragment_edit_limit");
    }

    @Override
    public void onLimitDialogPositiveClick(String limitString) {
        kcalLimit = Integer.parseInt(limitString);
        didSetLimit = true;
        updateKcalString();
        updateBar();
    }

    private void updateKcalString() {
        if (currentDay.getKcal() > kcalLimit) {
            tvDayKcal.setTextColor(getResources().getColor(R.color.colorError));
        } else {
            tvDayKcal.setTextColor(Color.BLACK);
        }
        tvDayKcal.setText(Integer.toString((int)Math.round(currentDay.getKcal())) + " / " + Integer.toString(kcalLimit) + " kcal");
    }
}
