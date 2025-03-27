package me.sirdas.diyet;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.Date;


public class DayBarAppWidget extends AppWidgetProvider {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor spEditor;
    private static Gson gson;
    private TextView tvWidgetBarCarb;
    private TextView tvWidgetBarProtein;
    private TextView tvWidgetBarFat;
    private View vWidgetBarEmpty;
    private static Day currentDay;
    private static int kcalLimit;

    static void updateAppWidget(Context c, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        spEditor = sharedPreferences.edit();
        gson = new Gson();
        if (sharedPreferences.contains("currentDay")) {
            String dayJson = sharedPreferences.getString("currentDay", "");
            currentDay = gson.fromJson(dayJson, Day.class);
            if (currentDay.getExpirationDate().before(new Date())) { //isExpired
                currentDay = new Day();
            }
        } else {
            currentDay = new Day();
        }
        kcalLimit = sharedPreferences.getInt("kcalLimit", 1);
        if (kcalLimit == 0) {
            kcalLimit = 1;
        }

        final Bundle bundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
        final int minWidth = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        final int minHeight = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int barWidth = (int) (minWidth * c.getResources().getDisplayMetrics().density);
        int barHeight = (int) (minHeight * c.getResources().getDisplayMetrics().density);
        //Log.d("width", Integer.toString(barWidth));
        RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.day_bar_app_widget);
        TextView tvWidgetBarCarb = new TextView(c);
        TextView tvWidgetBarProtein = new TextView(c);
        TextView tvWidgetBarFat = new TextView(c);
        TextView vWidgetBarEmpty = new TextView(c);
        double carbKcal = currentDay.getCarbKcal();
        double proteinKcal = currentDay.getProteinKcal();
        double fatKcal = currentDay.getFatKcal();
        int totalKcal = (int)Math.round(currentDay.getKcal());
        if (totalKcal > 0) {
            int kcalWidth = Math.round(totalKcal * barWidth / kcalLimit);
            if (kcalWidth > barWidth) {
                kcalWidth = barWidth;
            }
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
            int emptyWidth = barWidth - carbWidth - proteinWidth - fatWidth;
            tvWidgetBarCarb.measure(carbWidth,barHeight);
            tvWidgetBarProtein.measure(proteinWidth,barHeight);
            tvWidgetBarFat.measure(fatWidth,barHeight);
            vWidgetBarEmpty.measure(emptyWidth, barHeight);
            tvWidgetBarCarb.layout(0,0, carbWidth,barHeight);
            tvWidgetBarProtein.layout(carbWidth,0, carbWidth + proteinWidth, barHeight);
            tvWidgetBarFat.layout(carbWidth + proteinWidth,0, carbWidth + proteinWidth + fatWidth, barHeight);
            if (emptyWidth > 0) { // just in case
                vWidgetBarEmpty.setVisibility(View.VISIBLE);
            } else {
                vWidgetBarEmpty.setVisibility(View.INVISIBLE);
            }
            vWidgetBarEmpty.layout(carbWidth + proteinWidth + fatWidth, 0, carbWidth + proteinWidth + fatWidth + emptyWidth, barHeight);
        } else {
            tvWidgetBarCarb.layout(0,0, 0,0);
            tvWidgetBarProtein.layout(0,0, 0,0);
            tvWidgetBarFat.layout(0,0, 0,0);
            vWidgetBarEmpty.layout(0, 0, barWidth, barHeight);
        }
        tvWidgetBarCarb.setDrawingCacheEnabled(true);
        Bitmap bitmapCarb = tvWidgetBarCarb.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_carb, bitmapCarb);
        tvWidgetBarProtein.setDrawingCacheEnabled(true);
        Bitmap bitmapProtein = tvWidgetBarProtein.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_protein, bitmapProtein);
        tvWidgetBarFat.setDrawingCacheEnabled(true);
        Bitmap bitmapFat = tvWidgetBarFat.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_fat, bitmapFat);
        vWidgetBarEmpty.setDrawingCacheEnabled(true);
        Bitmap bitmapEmpty = vWidgetBarEmpty.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_empty, bitmapEmpty);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context c, AppWidgetManager manager, int appWidgetId, Bundle newOptions) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        spEditor = sharedPreferences.edit();
        gson = new Gson();
        if (sharedPreferences.contains("currentDay")) {
            String dayJson = sharedPreferences.getString("currentDay", "");
            currentDay = gson.fromJson(dayJson, Day.class);
            if (currentDay.getExpirationDate().before(new Date())) { //isExpired
                currentDay = new Day();
            }
        } else {
            currentDay = new Day();
        }
        kcalLimit = sharedPreferences.getInt("kcalLimit", 1);
        if (kcalLimit == 0) {
            kcalLimit = 1;
        }

        final Bundle bundle = manager.getAppWidgetOptions(appWidgetId);
        final int minWidth = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        final int minHeight = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int barWidth = (int) (minWidth * c.getResources().getDisplayMetrics().density);
        int barHeight = (int) (minHeight * c.getResources().getDisplayMetrics().density);
        RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.day_bar_app_widget);
        tvWidgetBarCarb = new TextView(c);
        tvWidgetBarProtein = new TextView(c);
        tvWidgetBarFat = new TextView(c);
        vWidgetBarEmpty = new TextView(c);
        double carbKcal = currentDay.getCarbKcal();
        double proteinKcal = currentDay.getProteinKcal();
        double fatKcal = currentDay.getFatKcal();
        int totalKcal = (int)Math.round(currentDay.getKcal());
        if (totalKcal > 0) {
            int kcalWidth = Math.round(totalKcal * barWidth / kcalLimit);
            if (kcalWidth > barWidth) {
                kcalWidth = barWidth;
            }
            //Log.d("bar", "update");
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
            int emptyWidth = barWidth - carbWidth - proteinWidth - fatWidth;
            tvWidgetBarCarb.measure(carbWidth,barHeight);
            tvWidgetBarProtein.measure(proteinWidth,barHeight);
            tvWidgetBarFat.measure(fatWidth,barHeight);
            vWidgetBarEmpty.measure(emptyWidth, barHeight);
            tvWidgetBarCarb.layout(0,0, carbWidth,barHeight);
            tvWidgetBarProtein.layout(carbWidth,0, carbWidth + proteinWidth, barHeight);
            tvWidgetBarFat.layout(carbWidth + proteinWidth,0, carbWidth + proteinWidth + fatWidth, barHeight);
            if (emptyWidth > 0) { // just in case
                vWidgetBarEmpty.setVisibility(View.VISIBLE);
            } else {
                vWidgetBarEmpty.setVisibility(View.INVISIBLE);
            }
            vWidgetBarEmpty.layout(carbWidth + proteinWidth + fatWidth, 0, carbWidth + proteinWidth + fatWidth + emptyWidth, barHeight);
        } else {
            tvWidgetBarCarb.layout(0,0, 0,0);
            tvWidgetBarProtein.layout(0,0, 0,0);
            tvWidgetBarFat.layout(0,0, 0,0);
            vWidgetBarEmpty.layout(0, 0, barWidth, barHeight);
        }
        tvWidgetBarCarb.setDrawingCacheEnabled(true);
        Bitmap bitmapCarb = tvWidgetBarCarb.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_carb, bitmapCarb);
        tvWidgetBarProtein.setDrawingCacheEnabled(true);
        Bitmap bitmapProtein = tvWidgetBarProtein.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_protein, bitmapProtein);
        tvWidgetBarFat.setDrawingCacheEnabled(true);
        Bitmap bitmapFat = tvWidgetBarFat.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_fat, bitmapFat);
        vWidgetBarEmpty.setDrawingCacheEnabled(true);
        Bitmap bitmapEmpty = vWidgetBarEmpty.getDrawingCache();
        views.setImageViewBitmap(R.id.v_widget_bar_empty, bitmapEmpty);
        // Instruct the widget manager to update the widget
        manager.updateAppWidget(appWidgetId, views);

    }
}

