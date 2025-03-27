package me.sirdas.diyet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener ocl) {
        onClickListener = ocl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_food_name);
            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }
    }

    private ArrayList<Food> foodArray;
    private ArrayList<Food> originalFoodArray;

    public FoodAdapter(ArrayList<Food> foodArray, ArrayList<Food> originalFoodArray) {
        this.foodArray = foodArray;
        this.originalFoodArray = originalFoodArray;
    }

    public ArrayList<Food> getFoodArray() {
        return this.foodArray;
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_food, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder viewHolder, int position) {
        Food f = this.foodArray.get(position);
        TextView tvName = viewHolder.tvName;
        tvName.setText(f.getName());
    }

    @Override
    public int getItemCount() {
        return this.foodArray.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                foodArray.clear();
                foodArray.addAll((ArrayList<Food>) results.values);
                notifyDataSetChanged();
            }
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Food> equalsFoodList = new LinkedList<>();
                List<Food> containsFoodList = new LinkedList<>();
                List<Food> lastFoodList = new LinkedList<>();
                List<Food> resultList = new ArrayList<>();
                //ArrayList<Food> originalFoodArray = new ArrayList<Food>(foodArray);
                if (constraint == null || constraint.length() == 0) {
                    results.count = originalFoodArray.size();
                    results.values = originalFoodArray;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    String[] constraintArray = ((String) constraint).split(" ");
                    for (int i = 0; i < originalFoodArray.size(); i++) {
                        Food f = originalFoodArray.get(i);
                        String foodNameFirst = f.getName().split(", ")[0].toLowerCase();

                        boolean foundEquals = false;
                        boolean foundContains = false;
                        for (String s : constraintArray) {

                            if (foodNameFirst.equals(s)) {
                                foundEquals = true;
                                break;
                            } else if (foodNameFirst.contains(s)) {
                                foundContains = true;
                            }
                        }
                        if (foundEquals) {
                            equalsFoodList.add(f);
                        } else if (foundContains) {
                            containsFoodList.add(f);
                        } else {
                            lastFoodList.add(f);
                        }
                    }
                    resultList.addAll(equalsFoodList);
                    resultList.addAll(containsFoodList);
                    resultList.addAll(lastFoodList);
                    results.count = resultList.size();
                    results.values = resultList;
                }
                return results;
            }
        };
        return filter;
    }
}