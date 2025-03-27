package me.sirdas.diyet;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodFragment extends Fragment implements FoodDetailFragment.FoodAddDialogListener {
    protected RecyclerView rvFood;
    protected SearchView svFood;
    protected FoodAdapter foodAdapter;
    protected FoodClient foodClient = new FoodClient();
    protected ArrayList<Food> foodArray = new ArrayList<>();
    protected ArrayList<Food> originalFoodArray = new ArrayList<>();
    protected ProgressBar pbFood;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Food f = foodArray.get(position);
            if (f.getReportAdded()) {
                Food newFood = new Food(f);
                //Log.d("foodError", Double.toString(f.getCarbGrams()));
                showAddDialog(newFood);
            } else {
                fetchFoodReport(f.getId(), position);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        rvFood = (RecyclerView) v.findViewById(R.id.rv_food);
        svFood = (SearchView) v.findViewById(R.id.sv_food);
        pbFood = (ProgressBar) v.findViewById(R.id.pb_food);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        svFood.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvFood.addItemDecoration(itemDecoration);
        foodAdapter = new FoodAdapter(foodArray, originalFoodArray);
        rvFood.setAdapter(foodAdapter);
        rvFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        svFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchFoods(query);
                svFood.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        foodAdapter.setOnItemClickListener(onClickListener);
        return v;
    }

    private void fetchFoods(final String query) {
        pbFood.setVisibility(ProgressBar.VISIBLE);
        foodClient.getFoods(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("json", response.toString());
                if (response != null) {
                    pbFood.setVisibility(ProgressBar.GONE);
                    foodArray.clear();
                    originalFoodArray.clear();
                    originalFoodArray.addAll(Food.fromJson(response));
                    Filter f = foodAdapter.getFilter();
                    f.filter(query);
                }
            }

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("json", response.toString());
//            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pbFood.setVisibility(ProgressBar.GONE);
            }
        });
    }

    private void fetchFoodReport(final String id, final int pos) {
        foodClient.getFoodReport(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("json", response.toString());
                if (response != null) {
                    Food food = foodArray.get(pos);
                    food.addReport(response);
                    showAddDialog(food);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    private void showAddDialog(Food food) {
        FragmentManager fm = getFragmentManager();
        FoodDetailFragment fdf = FoodDetailFragment.newInstance(food, false);
        fdf.setTargetFragment(FoodFragment.this, 300);
        fdf.show(fm, "fragment_food_detail");
    }

    @Override
    public void onFinishAddDialog(Food food) {
        ((MainActivity) getActivity()).addFood(food);
    }

}
