package me.sirdas.diyet;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;

public class Basket implements Serializable {
    private ArrayList<Food> foods;
    private double carbGrams;
    private double fatGrams;
    private double proteinGrams;
    private double carbKcal;
    private double fatKcal;
    private double proteinKcal;
    private double carbPerc;
    private double fatPerc;
    private double proteinPerc;
    private double kcal;
    private boolean isSaved;
    private String name;
    private Date addedDate; //use new API LocalTime in future


    public ArrayList<Food> getFoods() {
        return foods;
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

    public boolean isSaved() {
        return isSaved;
    }

    public String getName() {
        return name;
    }

    public Date getAddedDate() {
        return addedDate;
    }


    public void setAddedDate() {
        addedDate = new Date();
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Basket() {
        this.foods = new ArrayList<>();
        this.carbGrams = 0.0;
        this.fatGrams = 0.0;
        this.proteinGrams = 0.0;
        this.carbKcal = 0.0;
        this.fatKcal = 0.0;
        this.proteinKcal = 0.0;
        this.carbPerc = 0.0;
        this.fatPerc = 0.0;
        this.proteinPerc = 0.0;
        this.kcal = 0.0;
        this.name = "Basket";
        this.isSaved = false;
    }

    public Basket(Basket b) {
        this.foods = new ArrayList<Food>();
        for (Food f : b.foods) {
            this.foods.add(new Food(f));
        }
        this.carbGrams = b.carbGrams;
        this.fatGrams = b.fatGrams;
        this.proteinGrams = b.proteinGrams;
        this.carbKcal = b.carbKcal;
        this.fatKcal = b.fatKcal;
        this.proteinKcal = b.proteinKcal;
        this.carbPerc = b.carbPerc;
        this.fatPerc = b.fatPerc;
        this.proteinPerc = b.proteinPerc;
        this.kcal = b.kcal;
        this.name = b.name;
        this.isSaved = b.isSaved;
    }

    public Basket(Basket b, String n) {
        this.foods = new ArrayList<Food>();
        for (Food f : b.foods) {
            this.foods.add(new Food(f));
        }
        this.carbGrams = b.carbGrams;
        this.fatGrams = b.fatGrams;
        this.proteinGrams = b.proteinGrams;
        this.carbKcal = b.carbKcal;
        this.fatKcal = b.fatKcal;
        this.proteinKcal = b.proteinKcal;
        this.carbPerc = b.carbPerc;
        this.fatPerc = b.fatPerc;
        this.proteinPerc = b.proteinPerc;
        this.kcal = b.kcal;
        this.name = n;
        isSaved = true;
    }

    public void add(Food f) {
        this.isSaved = false;
        this.foods.add(f);
        this.carbGrams += f.getCarbGrams();
        this.fatGrams += f.getFatGrams();
        this.proteinGrams += f.getProteinGrams();
        this.carbKcal += f.getCarbKcal();
        this.fatKcal += f.getFatKcal();
        this.proteinKcal += f.getProteinKcal();
        this.kcal += f.getKcal();
        this.carbPerc = (this.carbKcal / this.kcal) * 100;
        this.fatPerc = (this.fatKcal / this.kcal) * 100;
        this.proteinPerc = (this.proteinKcal / this.kcal) * 100;

    }

    public boolean delete(Food f) {
        if (!this.foods.remove(f)) {
            return false;
        }
        this.isSaved = false;
        this.carbGrams -= f.getCarbGrams();
        this.fatGrams -= f.getFatGrams();
        this.proteinGrams -= f.getProteinGrams();
        this.carbKcal -= f.getCarbKcal();
        this.fatKcal -= f.getFatKcal();
        this.proteinKcal -= f.getProteinKcal();
        this.kcal -= f.getKcal();
        if (this.kcal <= 0) {
            this.carbPerc = 0;
            this.fatPerc = 0;
            this.proteinPerc = 0;
        } else {
            this.carbPerc = (this.carbKcal / this.kcal) * 100;
            this.fatPerc = (this.fatKcal / this.kcal) * 100;
            this.proteinPerc = (this.proteinKcal / this.kcal) * 100;
        }
        return true;
    }

    public boolean edit(Food f, double newQuant, double newUnit) {
        if (!this.foods.contains(f)) {
            return false;
        }
        this.isSaved = false;
        this.carbGrams -= f.getCarbGrams();
        this.fatGrams -= f.getFatGrams();
        this.proteinGrams -= f.getProteinGrams();
        this.carbKcal -= f.getCarbKcal();
        this.fatKcal -= f.getFatKcal();
        this.proteinKcal -= f.getProteinKcal();
        this.kcal -= f.getKcal();
        if (this.kcal <= 0) {
            this.carbPerc = 0;
            this.fatPerc = 0;
            this.proteinPerc = 0;
        } else {
            this.carbPerc = (this.carbKcal / this.kcal) * 100;
            this.fatPerc = (this.fatKcal / this.kcal) * 100;
            this.proteinPerc = (this.proteinKcal / this.kcal) * 100;
        }
        f.updateQuantAndUnit(newQuant, newUnit);
        this.carbGrams += f.getCarbGrams();
        this.fatGrams += f.getFatGrams();
        this.proteinGrams += f.getProteinGrams();
        this.carbKcal += f.getCarbKcal();
        this.fatKcal += f.getFatKcal();
        this.proteinKcal += f.getProteinKcal();
        this.kcal += f.getKcal();
        this.carbPerc = (this.carbKcal / this.kcal) * 100;
        this.fatPerc = (this.fatKcal / this.kcal) * 100;
        this.proteinPerc = (this.proteinKcal / this.kcal) * 100;
        return true;
    }

    public SpannableStringBuilder toSpannableString() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        DecimalFormat df1 = new DecimalFormat(".#");
        for (int i = 0; i < this.foods.size(); i++) {
            Food food = this.foods.get(i);
            String quant = df1.format(food.getQuantity() * food.getUnit());
            String name = food.getName();
            ssb.append(quant + " g  ", new StyleSpan(ITALIC), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(name, new StyleSpan(BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i != this.foods.size() - 1) {
                ssb.append("\n\n");
            }
        }
        return ssb;
    }
}
