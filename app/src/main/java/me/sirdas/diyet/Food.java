package me.sirdas.diyet;

import android.util.Log;

import org.json.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Food implements Serializable {

    private String name;
    private String id;
    private boolean reportAdded;
    private double carbFactor;
    private double fatFactor;
    private double proteinFactor;
    private double carbGrams;
    private double fatGrams;
    private double proteinGrams;
    private double carbKcal;
    private double fatKcal;
    private double proteinKcal;
    private double carbGramsOriginal;
    private double fatGramsOriginal;
    private double proteinGramsOriginal;
    private double carbKcalOriginal;
    private double fatKcalOriginal;
    private double proteinKcalOriginal;
    private double carbPerc;
    private double fatPerc;
    private double proteinPerc;
    private double kcal;
    private List<String> measureLabels;
    private List<Double> measureUnits;
    private double quantity;
    private double unit;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean getReportAdded() {
        return reportAdded;
    }

    public double getCarbFactor() {
        return carbFactor;
    }

    public double getFatFactor() {
        return fatFactor;
    }

    public double getProteinFactor() {
        return proteinFactor;
    }

    public double getCarbGrams() {
        return carbGrams;
    }

    public double getFatGrams() {
        return fatGrams;
    }

    public double getProteinGrams() {
        return proteinGrams;
    }

    public double getCarbKcal() {
        return carbKcal;
    }

    public double getFatKcal() {
        return fatKcal;
    }

    public double getProteinKcal() {
        return proteinKcal;
    }

    public double getCarbPerc() {
        return carbPerc;
    }

    public double getFatPerc() {
        return fatPerc;
    }

    public double getProteinPerc() {
        return proteinPerc;
    }

    public double getKcal() {
        return kcal;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getUnit() {
        return unit;
    }

    public double getUnit(int pos) {
        return this.measureUnits.get(pos);
    }

    public boolean isReportAdded() {
        return reportAdded;
    }

    public double getCarbGramsOriginal() {
        return carbGramsOriginal;
    }

    public double getFatGramsOriginal() {
        return fatGramsOriginal;
    }

    public double getProteinGramsOriginal() {
        return proteinGramsOriginal;
    }

    public double getCarbKcalOriginal() {
        return carbKcalOriginal;
    }

    public double getFatKcalOriginal() {
        return fatKcalOriginal;
    }

    public double getProteinKcalOriginal() {
        return proteinKcalOriginal;
    }

    public List<String> getMeasureLabels() {
        return measureLabels;
    }

    public List<Double> getMeasureUnits() {
        return measureUnits;
    }

    public Food(String name, String id) {
        this.name = name;
        this.id = id;
        this.reportAdded = false;
        this.measureLabels = new ArrayList<>();
        this.measureUnits = new ArrayList<>();
        this.quantity = 100;
        this.unit = 1;
    }

    public Food(Food f) { //copy constructor
        this.name = f.name;
        this.id = f.id;
        this.reportAdded = f.reportAdded;
        this.carbFactor = f.carbFactor;
        this.fatFactor = f.fatFactor;
        this.proteinFactor = f.proteinFactor;
        this.carbGrams = f.carbGrams;
        this.fatGrams = f.fatGrams;
        this.proteinGrams = f.proteinGrams;
        this.carbKcal = f.carbKcal;
        this.fatKcal = f.fatKcal;
        this.proteinKcal = f.proteinKcal;
        this.carbGramsOriginal = f.carbGramsOriginal;
        this.fatGramsOriginal = f.fatGramsOriginal;
        this.proteinGramsOriginal = f.proteinGramsOriginal;
        this.carbKcalOriginal = f.carbKcalOriginal;
        this.fatKcalOriginal = f.fatKcalOriginal;
        this.proteinKcalOriginal = f.proteinKcalOriginal;
        this.carbPerc = f.carbPerc;
        this.fatPerc = f.fatPerc;
        this.proteinPerc = f.proteinPerc;
        this.kcal = f.kcal;
        this.measureLabels = f.measureLabels;
        this.measureUnits = f.measureUnits;
        this.quantity = f.quantity;
        this.unit = f.unit;
    }

    public static Food fromJson(JSONObject jsonObject) {
        Food f;
        try {
            f = new Food(jsonObject.getString("description"), jsonObject.getString("fdcId"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return f;
    }

    public static ArrayList<Food> fromJson(JSONArray jsonArray) {
        ArrayList<Food> foods = new ArrayList<Food>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject foodJson = null;
            try {
                foodJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Food f = Food.fromJson(foodJson);
            if (f != null) {
                foods.add(f);
            }
        }
        return foods;
    }

    public void addReport(final JSONObject jsonObject) {
        try {
            final JSONArray factors = jsonObject.getJSONArray("nutrientConversionFactors");
            for (int i = 0; i < factors.length(); i++) {
                JSONObject factor = factors.getJSONObject(i);
                if (factor.getString("type").equals(".CalorieConversionFactor")) {
                    this.carbFactor = factor.getDouble("carbohydrateValue");
                    this.fatFactor = factor.getDouble("fatValue");
                    this.proteinFactor = factor.getDouble("proteinValue");
                }
            }

            if (this.carbFactor == 0) {
                this.carbFactor = 4;
            }
            if (this.fatFactor == 0) {
                this.fatFactor = 9;
            }
            if (this.proteinFactor == 0) {
                this.proteinFactor = 4;
            }
            final JSONArray nutrients = jsonObject.getJSONArray("foodNutrients");
            final JSONObject protein = nutrients.getJSONObject(0);
            final JSONObject fat = nutrients.getJSONObject(1);
            final JSONObject carb = nutrients.getJSONObject(2);
            final JSONArray measures = jsonObject.getJSONArray("foodPortions");
            DecimalFormat df1 = new DecimalFormat(".#");
            this.measureLabels.add("gram (g)");
            this.measureUnits.add(1.0);
            for (int i = 0; i < measures.length(); i++) {
                JSONObject measure = measures.getJSONObject(i);
                double measureUnit = measure.getDouble("gramWeight");
                String measureLabel = measure.getString("modifier") + " (" + df1.format(measureUnit) + " g)";
                this.measureLabels.add(measureLabel);
                this.measureUnits.add(measureUnit);
            }
            this.proteinGramsOriginal = protein.getDouble("amount");
            this.fatGramsOriginal = fat.getDouble("amount");
            this.carbGramsOriginal = carb.getDouble("amount");
            this.proteinKcalOriginal = this.proteinGramsOriginal * this.proteinFactor;
            this.fatKcalOriginal = this.fatGramsOriginal * this.fatFactor;
            this.carbKcalOriginal = this.carbGramsOriginal * this.carbFactor;
            this.calculateGramsAndKcal();
            this.proteinPerc = (this.proteinKcal / this.kcal) * 100;
            this.fatPerc = (this.fatKcal / this.kcal) * 100;
            this.carbPerc = (this.carbKcal / this.kcal) * 100;
            this.reportAdded = true;
            Log.d("foodError", Double.toString(this.carbPerc));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantAndUnit(double q, double u) {
        this.quantity = q;
        this.unit = u;
        this.calculateGramsAndKcal();
    }

    public void calculateGramsAndKcal() {
        double ratio = this.quantity * this.unit / 100;
        this.proteinGrams = this.proteinGramsOriginal * ratio;
        this.fatGrams = this.fatGramsOriginal * ratio;
        this.carbGrams = this.carbGramsOriginal * ratio;
        this.proteinKcal = this.proteinKcalOriginal * ratio;
        this.fatKcal = this.fatKcalOriginal * ratio;
        this.carbKcal = this.carbKcalOriginal * ratio;
        this.kcal = this.proteinKcal + this.fatKcal + this.carbKcal;
    }


}
