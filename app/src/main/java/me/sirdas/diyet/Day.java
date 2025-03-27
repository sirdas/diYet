package me.sirdas.diyet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Day {

    private ArrayList<Basket> baskets;
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
    private Date expirationDate;

    public ArrayList<Basket> getBaskets() {
        return baskets;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Day() {
        this.baskets = new ArrayList<>();
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        this.expirationDate = calendar.getTime();
    }

    public void add(Basket b) {
        b.setAddedDate();
        this.baskets.add(b);
        this.carbGrams += b.getCarbGrams();
        this.fatGrams += b.getFatGrams();
        this.proteinGrams += b.getProteinGrams();
        this.carbKcal += b.getCarbKcal();
        this.fatKcal += b.getFatKcal();
        this.proteinKcal += b.getProteinKcal();
        this.kcal += b.getKcal();
        this.carbPerc = (this.carbKcal / this.kcal) * 100;
        this.fatPerc = (this.fatKcal / this.kcal) * 100;
        this.proteinPerc = (this.proteinKcal / this.kcal) * 100;
    }

    public boolean delete(Basket b) {
        if (!this.baskets.remove(b)) {
            return false;
        }
        this.carbGrams -= b.getCarbGrams();
        this.fatGrams -= b.getFatGrams();
        this.proteinGrams -= b.getProteinGrams();
        this.carbKcal -= b.getCarbKcal();
        this.fatKcal -= b.getFatKcal();
        this.proteinKcal -= b.getProteinKcal();
        this.kcal -= b.getKcal();
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
}
