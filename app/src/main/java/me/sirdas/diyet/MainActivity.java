package me.sirdas.diyet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FoodDetailFragment.FoodModifyDialogListener, EditRecipeNameDialogFragment.EditRecipeNameDialogListener, OverwriteRecipeNameDialogFragment.OverwriteRecipeNameDialogListener {
    protected BottomNavigationView bnwBottom;
    protected ConstraintLayout clRoot;
    protected ConstraintLayout bsBottom;
    protected CoordinatorLayout clSheet;
    protected NonScrollBottomSheetBehavior bsb;
    protected Fragment activeFragment;
    protected Basket currentBasket;
    protected ViewPager vpBasketDetail;
    protected BasketFoodAdapter bfa;
    protected ImageButton ibBasket;
    protected TextView tvBasketKcal;
    protected TextView tvBasketName;
    protected Button bBasketSave;
    protected Button bBasketAdd;
    protected Fragment tf;
    protected Fragment ff;
    protected Fragment rf;
    protected FragmentManager fm;
    protected ListView lvBasket;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor spEditor;
    protected Gson gson;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Food food = (Food) view.getTag();
            switch (view.getId()) {
                case R.id.ib_basket_food_delete:
                    deleteFood(food);
                    break;
                case R.id.cl_basket_food:
                    showModifyFoodDialog(food);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tf = new TodayFragment();
        ff = new FoodFragment();
        rf = new RecipesFragment();
        fm = getSupportFragmentManager();
        activeFragment = tf;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = sharedPreferences.edit();
        gson = new Gson();
        if (sharedPreferences.contains("currentBasket")) {
            String basketJson = sharedPreferences.getString("currentBasket", "");
            currentBasket = gson.fromJson(basketJson, Basket.class);
        } else {
            currentBasket = new Basket();
        }
        if (sharedPreferences.contains("currentDay")) {
            String dayJson = sharedPreferences.getString("currentDay", "");
            Day possibleDay = gson.fromJson(dayJson, Day.class);
            if (possibleDay.getExpirationDate().before(new Date())) { //isExpired
                ((TodayFragment) tf).currentDay = new Day();
            } else {
                ((TodayFragment) tf).currentDay = possibleDay;
            }
        } else {
            ((TodayFragment) tf).currentDay = new Day();
        }
        if (sharedPreferences.contains("kcalLimit")) {
            ((TodayFragment) tf).didSetLimit = true;
            ((TodayFragment) tf).kcalLimit = sharedPreferences.getInt("kcalLimit", 2000);
            if (((TodayFragment) tf).kcalLimit == 0) {
                ((TodayFragment) tf).kcalLimit = 1;
            }
        } else {
            ((TodayFragment) tf).didSetLimit = false;
        }
        if (sharedPreferences.contains("savedRecipes")) {
            String recipesJson = sharedPreferences.getString("savedRecipes", "");
            ((RecipesFragment) rf).savedRecipes = gson.fromJson(recipesJson, new TypeToken<ArrayList<Basket>>() {
            }.getType());
            String recipeNamesJson = sharedPreferences.getString("savedRecipeNames", "");
            ((RecipesFragment) rf).savedRecipeNames = gson.fromJson(recipeNamesJson, new TypeToken<HashSet<String>>() {
            }.getType());
        } else {
            ((RecipesFragment) rf).savedRecipes = new ArrayList<Basket>();
            ((RecipesFragment) rf).savedRecipeNames = new HashSet<String>();
        }
        /*if (sharedPreferences.contains("savedFoods")) {

        } else {

        }*/
        bsBottom = findViewById(R.id.cl_basket);
        clSheet = findViewById(R.id.cl_sheet);
        ibBasket = findViewById(R.id.ib_basket);
        tvBasketKcal = findViewById(R.id.tv_basket_kcal);
        bBasketAdd = findViewById(R.id.b_basket_add);
        bBasketSave = findViewById(R.id.b_basket_save);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_root);
        clRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int originalHeight;
            boolean laidOut = false;
            @Override
            public void onGlobalLayout() {
                if (laidOut) {
                    int height = clRoot.getHeight();
                    if (height < originalHeight) {
                        bnwBottom.setVisibility(View.GONE);
                        bsBottom.setVisibility(View.GONE);
                    } else {
                        if (activeFragment != tf) {
                            bsBottom.setVisibility(View.VISIBLE);
                        }
                        bnwBottom.setVisibility(View.VISIBLE);
                    }
                    //Log.d("MyActivity", Integer.toString(height));
                } else {
                    originalHeight = clRoot.getHeight();
                    laidOut = true;
                }
                clRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        bnwBottom = (BottomNavigationView) findViewById(R.id.bnw_bottom);
        int bnwHeight = bnwBottom.getHeight();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int windowHeight = displayMetrics.heightPixels;
        tvBasketName = (TextView) findViewById(R.id.tv_basket_name);
        bsb = (NonScrollBottomSheetBehavior) NonScrollBottomSheetBehavior.from(bsBottom);
        bsb.setBottomSheetCallback(createBottomSheetCallback());
        bsBottom.setVisibility(View.GONE);
        fm.beginTransaction().add(R.id.ll_fragment, rf, "3").hide(rf).commit();
        fm.beginTransaction().add(R.id.ll_fragment, ff, "2").hide(ff).commit();
        fm.beginTransaction().add(R.id.ll_fragment, tf, "1").commit();
        bnwBottom.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        switch (menuItem.getItemId()) {
                            case R.id.dest_today:
                                bsBottom.setVisibility(View.GONE);
                                fm.beginTransaction().hide(activeFragment).show(tf).commit();
                                activeFragment = tf;
                                break;
                            case R.id.dest_food:
                                bsBottom.setVisibility(View.VISIBLE);
                                fm.beginTransaction().hide(activeFragment).show(ff).commit();
                                activeFragment = ff;
                                break;
                            case R.id.dest_recipes:
                                bsBottom.setVisibility(View.VISIBLE);
                                fm.beginTransaction().hide(activeFragment).show(rf).commit();
                                activeFragment = rf;
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
        bfa = new BasketFoodAdapter(this, currentBasket.getFoods());
        bfa.setOnItemClickListener(onClickListener);
        lvBasket = (ListView) findViewById(R.id.lv_basket);
        lvBasket.setAdapter(bfa);
        vpBasketDetail = (ViewPager) findViewById(R.id.vp_basket_detail);
        vpBasketDetail.setPageMargin(50);
        setUpBasketDetailPager();
        tvBasketName.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
        bsBottom.setOnClickListener(this);
        ibBasket.setOnClickListener(this);
        bBasketAdd.setOnClickListener(this);
        bBasketSave.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String basketJson = gson.toJson(currentBasket);
        spEditor.putString("currentBasket", basketJson);
        String dayJson = gson.toJson(((TodayFragment) tf).currentDay);
        spEditor.putString("currentDay", dayJson);
        spEditor.putInt("kcalLimit", ((TodayFragment) tf).kcalLimit);
        String recipesJson = gson.toJson(((RecipesFragment) rf).savedRecipes);
        spEditor.putString("savedRecipes", recipesJson);
        String recipeNamesJson = gson.toJson(((RecipesFragment) rf).savedRecipeNames);
        spEditor.putString("savedRecipeNames", recipeNamesJson);
        spEditor.apply();

        Intent intent = new Intent(this, DayBarAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), DayBarAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private BottomSheetBehavior.BottomSheetCallback createBottomSheetCallback() {
        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
                new BottomSheetBehavior.BottomSheetCallback() {
                    boolean wasCollapsed = true;
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                if (wasCollapsed) {
                                    tvBasketName.setText("Basket");
                                    if (currentBasket.getFoods().isEmpty()) {
                                        tvBasketKcal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                        tvBasketKcal.setText("No food in basket!");
                                        vpBasketDetail.setVisibility(View.GONE);
                                    } else {
                                        tvBasketKcal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                                        tvBasketKcal.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
                                        vpBasketDetail.setVisibility(View.VISIBLE);
                                    }
                                    ibBasket.setImageResource(R.drawable.ic_basket_close);
                                    wasCollapsed = false;
                                } else {
                                    tvBasketName.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
                                    ibBasket.setImageResource(R.drawable.ic_food_cart);
                                    wasCollapsed = true;
                                }
                                break;
                            case BottomSheetBehavior.STATE_HIDDEN:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                };
        return bottomSheetCallback;
    }

    protected void setUpBasketDetailPager() {
        vpBasketDetail.setAdapter(new BasketDetailPagerAdapter(this, currentBasket));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_basket:
                if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.d("bsb", "it work");
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;

            case R.id.ib_basket:
                if (bsb.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.d("bsb", "it work");
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.d("bsb", "it work");
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;

            case R.id.b_basket_add:
                if (!currentBasket.getFoods().isEmpty()) {
                    ((TodayFragment) tf).addBasket(currentBasket);
                    currentBasket = new Basket();
                    bfa = new BasketFoodAdapter(this, currentBasket.getFoods());
                    lvBasket.setAdapter(bfa);
                    bfa.setOnItemClickListener(onClickListener);
                    setUpBasketDetailPager();
                    tvBasketName.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
                    bnwBottom.setSelectedItemId(R.id.dest_today);
                }
                break;
            case R.id.b_basket_save:
                if (!currentBasket.getFoods().isEmpty()) {
                    showEditRecipeNameDialog(currentBasket);
                }
                break;
        }
    }

    protected void addFood(Food food) {
        currentBasket.add(food);
        setUpBasketDetailPager();
        bfa.notifyDataSetChanged();
        if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            tvBasketName.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
        }
    }

    protected void addRecipe(Basket recipe) {
        for (Food food : recipe.getFoods()) {
            currentBasket.add(food);
        }
        setUpBasketDetailPager();
        bfa.notifyDataSetChanged();
        if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            tvBasketName.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
        }
    }

    protected void deleteFood(Food food) {
        if (currentBasket.delete(food)) {
            setUpBasketDetailPager();
            bfa.notifyDataSetChanged();
            if (currentBasket.getFoods().isEmpty()) {
                tvBasketKcal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tvBasketKcal.setText("No food in basket!");
                vpBasketDetail.setVisibility(View.GONE);
            } else {
                tvBasketKcal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvBasketKcal.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
                vpBasketDetail.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void showEditRecipeNameDialog(Basket basket) {
        EditRecipeNameDialogFragment editRecipeNameDialogFragment = EditRecipeNameDialogFragment.newInstance(basket);
        editRecipeNameDialogFragment.show(fm, "fragment_edit_recipe_name");
    }

    protected void showModifyFoodDialog(Food food) {
        FoodDetailFragment fdf = FoodDetailFragment.newInstance(food, true);
        fdf.show(fm, "fragment_food_detail");
    }

    protected void showOverwriteRecipeNameDialog(Basket basket, String nameString) {
        OverwriteRecipeNameDialogFragment overwriteRecipeNameDialogFragment = OverwriteRecipeNameDialogFragment.newInstance(basket, nameString);
        overwriteRecipeNameDialogFragment.show(fm, "fragment_overwrite_recipe_name");
    }

    @Override
    public void onRecipeNameDialogPositiveClick(Basket basket, String nameString) {
        Log.d("recipe", ((RecipesFragment) rf).savedRecipeNames.toString());
        if (((RecipesFragment) rf).savedRecipeNames.contains(nameString)) {
            showOverwriteRecipeNameDialog(basket, nameString);
        } else {
            basket.setSaved(true);
            basket.setName(nameString);
            ((RecipesFragment) rf).savedRecipes.add(new Basket(basket, nameString));
            ((RecipesFragment) rf).savedRecipeNames.add(nameString);
            ((RecipesFragment) rf).ra.notifyDataSetChanged();
            Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOverwriteRecipeDialogPositiveClick(Basket basket, String nameString) {
        for (int i = 0; i < ((RecipesFragment) rf).savedRecipes.size(); i++) {
            Basket b = ((RecipesFragment) rf).savedRecipes.get(i);
            if (b.getName().equals(nameString)) {
                for (Basket dayBasket : ((TodayFragment) tf).currentDay.getBaskets()) {
                    if (dayBasket.getName().equals(nameString)) {
                        dayBasket.setSaved(false);
                        dayBasket.setName("Basket");
                        ((TodayFragment) tf).da.notifyDataSetChanged();
                    }
                }
                b.setSaved(false);
                b.setName("Basket");
                basket.setSaved(true);
                basket.setName(nameString);
                ((RecipesFragment) rf).savedRecipes.set(i, new Basket(basket, nameString));
                break;
            }
        }
        ((RecipesFragment) rf).ra.notifyDataSetChanged();
        Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishModifyDialog(Food f, double newQuant, double newUnit) {
        currentBasket.edit(f, newQuant, newUnit);
        setUpBasketDetailPager();
        bfa.notifyDataSetChanged();
        tvBasketKcal.setText(Integer.toString((int) Math.round(currentBasket.getKcal())) + " kcal");
    }
}
