package me.sirdas.diyet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class FoodDetailFragment extends DialogFragment implements View.OnClickListener {

    private EditText etQuant;
    private TextView tvFoodName;
    private Food originalFood;
    private Food food;
    private ViewPager vpFoodDetail;
    private Spinner sQuant;
    private TextView tvFoodKcal;
    private double currentQuant = 100.0;
    private double currentUnit = 1.0;
    private boolean modifyingFood;

    public interface FoodAddDialogListener {
        void onFinishAddDialog(Food f);
    }

    public interface FoodModifyDialogListener {
        void onFinishModifyDialog(Food f, double newQuant, double newUnit);
    }

    public void sendBackResult() {
        FoodAddDialogListener listener = (FoodAddDialogListener) getTargetFragment();
        listener.onFinishAddDialog(food);
        dismiss();
    }

    public void sendBackModifyResult() {
        FoodModifyDialogListener listener = (FoodModifyDialogListener) getActivity();
        listener.onFinishModifyDialog(originalFood, currentQuant, currentUnit);
        dismiss();
    }

    public FoodDetailFragment() {}

    public static FoodDetailFragment newInstance(Food food, boolean modifyingFood) {
        FoodDetailFragment frag = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("food", food);
        args.putBoolean("modifyingFood", modifyingFood);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_food_detail, container);
        tvFoodName = (TextView) v.findViewById(R.id.tv_basket_food_name);
        etQuant = (EditText) v.findViewById(R.id.et_food_quant);
        originalFood = (Food) getArguments().getSerializable("food");
        food = new Food(originalFood);
        modifyingFood = getArguments().getBoolean("modifyingFood");
        if (!modifyingFood) {
            food.updateQuantAndUnit(currentQuant, currentUnit);
        } else {
            currentQuant = food.getQuantity() * food.getUnit();
            //DecimalFormat df1 = new DecimalFormat(".#");
        }
        tvFoodName.setText(food.getName());
        vpFoodDetail = (ViewPager) v.findViewById(R.id.vp_food_detail);
        setUpFoodDetailPager(food);
        sQuant = (Spinner) v.findViewById(R.id.s_quant);
        final List<String> measureLabels = food.getMeasureLabels();
        ArrayAdapter<String> aaQuant = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_quant, measureLabels);
        aaQuant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sQuant.setAdapter(aaQuant);
        tvFoodKcal = (TextView) v.findViewById(R.id.tv_food_kcal);
        tvFoodKcal.setText(Integer.toString((int)Math.round(food.getKcal())) + " kcal");
        etQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String sString = s.toString();
                if (sString.equals("") || sString.equals(".")) {
                    currentQuant = 0;
                } else if (sString.equals(",") || sString.equals("-")) {
                    currentQuant = 0;
                    etQuant.setText("");
                } else {
                    currentQuant = Double.parseDouble(s.toString());
                }
                food.updateQuantAndUnit(currentQuant, currentUnit);
                tvFoodKcal.setText(Integer.toString((int)Math.round(food.getKcal())) + " kcal");
                vpFoodDetail.setAdapter(new FoodDetailPagerAdapter(getContext(), food));
            }
        });
        sQuant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentUnit = food.getUnit(position);
                if (!etQuant.getText().toString().equals("")) { //this if statement helps show kcal for 100g when fragment first started
                    etQuant.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        Button bFoodAdd = (Button) v.findViewById(R.id.b_food_add);
        if (modifyingFood) {
            bFoodAdd.setText("Update Basket");
        }
        Button bFoodCancel = (Button) v.findViewById(R.id.b_food_cancel);
        bFoodAdd.setOnClickListener(this);
        bFoodCancel.setOnClickListener(this);
        return v;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setUpFoodDetailPager(Food f) {
        vpFoodDetail.setAdapter(new FoodDetailPagerAdapter(getContext(), f));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.b_food_add: {
                if (!etQuant.getText().toString().equals("") && currentQuant != 0) {
                    if (modifyingFood) {
                        sendBackModifyResult();
                    } else {
                        sendBackResult();
                    }
                }
                break;
            }

            case R.id.b_food_cancel: {
                dismiss();
                break;
            }
        }
    }
}