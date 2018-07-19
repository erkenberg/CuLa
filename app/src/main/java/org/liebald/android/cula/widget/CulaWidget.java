package org.liebald.android.cula.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Pojos.StatisticsLastTrainingDate;
import org.liebald.android.cula.utilities.InjectorUtils;

import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class CulaWidget extends AppWidgetProvider {

    private static final String TAG = CulaWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(TAG, "");
        CulaRepository repository = InjectorUtils.provideRepository();
        LiveData<StatisticsLastTrainingDate> liveData = repository.getLastTrainingDate();
        liveData.observeForever(new Observer<StatisticsLastTrainingDate>() {
            @Override
            public void onChanged(@Nullable StatisticsLastTrainingDate date) {
                liveData.removeObserver(this);
                updateLastLearned(context, appWidgetManager, appWidgetId, date);
            }
        });

    }

    private static void updateLastLearned(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, StatisticsLastTrainingDate date) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cula_widget);

        String text;
        if (date == null || date.lastActive == null) {
            text = context.getString(R.string.widget_last_learned_never);
            Log.d(TAG, "date null");
        } else {
            long hoursSinceLastLearned = (new Date().getTime() - date.lastActive.getTime()) /
                    (1000 * 60 * 60);
            if (hoursSinceLastLearned < 24)
                text = context.getString(R.string.widget_last_learned_recently);
            else if (hoursSinceLastLearned < 24 * 3)
                text = context.getString(R.string.widget_last_learned_last_3_days);
            else if (hoursSinceLastLearned < 24 * 7)
                text = context.getString(R.string.widget_last_learned_last_7_days);
            else
                text = context.getString(R.string.widget_last_learned_not);
            Log.d(TAG, "date of last activity: " + date.lastActive.toString());
        }
        views.setTextViewText(R.id.tv_widget_last_learned, text);

        // Instruct the widget manager to update the widget
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
}

