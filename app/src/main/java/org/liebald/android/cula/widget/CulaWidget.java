package org.liebald.android.cula.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Pojos.LessonKnowledgeLevel;
import org.liebald.android.cula.data.database.Pojos.StatisticsLastTrainingDate;
import org.liebald.android.cula.ui.main.MainActivity;
import org.liebald.android.cula.utilities.InjectorUtils;

import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class CulaWidget extends AppWidgetProvider {

    private static final String TAG = CulaWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.cula_widget);
        Intent configIntent = new Intent(context, MainActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget, configPendingIntent);

        CulaRepository repository = InjectorUtils.provideRepository();
        LiveData<StatisticsLastTrainingDate> lastTrainingDate = repository.getLastTrainingDate();
        lastTrainingDate.observeForever(new Observer<StatisticsLastTrainingDate>() {
            @Override
            public void onChanged(@Nullable StatisticsLastTrainingDate date) {
                lastTrainingDate.removeObserver(this);
                updateLastLearned(context, appWidgetManager, appWidgetId, remoteViews, date);
            }
        });

        LiveData<LessonKnowledgeLevel> worstLesson = repository.getWorstLesson();
        worstLesson.observeForever(new Observer<LessonKnowledgeLevel>() {
            @Override
            public void onChanged(@Nullable LessonKnowledgeLevel lessonKnowledgeLevel) {
                worstLesson.removeObserver(this);
                updateWorstLesson(context, appWidgetManager, appWidgetId, remoteViews,
                        lessonKnowledgeLevel);
            }
        });


    }

    /**
     * Updates the widget text for the reminder when a user trained the last time.
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param views
     * @param date
     */
    private static void updateLastLearned(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, RemoteViews views, StatisticsLastTrainingDate date) {
        // Construct the RemoteViews object

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

    /**
     * Update the text for the worst Lesson in the widget.
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param views
     * @param lessonKnowledgeLevel
     */
    private static void updateWorstLesson(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, RemoteViews views, LessonKnowledgeLevel lessonKnowledgeLevel) {
        if (lessonKnowledgeLevel == null)
            views.setViewVisibility(R.id.tv_widget_lesson_proposal, View.GONE);
        else {
            views.setViewVisibility(R.id.tv_widget_lesson_proposal, View.VISIBLE);
            views.setTextViewText(R.id.tv_widget_lesson_proposal, context.getString(R.string
                    .widget_lesson_proposal, lessonKnowledgeLevel.lessonName));
            Log.d(TAG, "Worst Lesson: " + lessonKnowledgeLevel.lessonName + " average: " +
                    "" + lessonKnowledgeLevel.average);
        }
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

