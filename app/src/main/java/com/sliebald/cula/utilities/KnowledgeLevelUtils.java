package com.sliebald.cula.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.sliebald.cula.MyApplication;
import com.sliebald.cula.R;

import androidx.core.content.res.ResourcesCompat;

public class KnowledgeLevelUtils {
    /**
     * The absolute minimum of the KnowledgeLevel.
     */
    private static final int LEVEL_MIN = 0;

    /**
     * The absolute maximum of the KnowledgeLevel.
     */
    private static final int LEVEL_MAX = 5;


    /**
     * Resolves the given knowledgeLevel to color.
     *
     * @param context        Context required to access the resources.
     * @param knowledgeLevel The knowledgeLevel.
     * @return The color of the given KnowledgeLevel.
     */
    public static int getColorByKnowledgeLevel(Context context, double knowledgeLevel) {
        Resources res = context.getResources();
        if (knowledgeLevel < 1)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_1, null);
        else if (knowledgeLevel < 2)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_2, null);
        else if (knowledgeLevel < 3)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_3, null);
        else if (knowledgeLevel < 4)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_4, null);
        else
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_5, null);
    }

    /**
     * Resolves the given knowledgeLevel to the fitting name.
     *
     * @param context        Context required to access the resources.
     * @param knowledgeLevel The knowledgeLevel.
     * @return The name of the given KnowledgeLevel
     */
    public static String getNameByKnowledgeLevel(Context context, double knowledgeLevel) {
        Resources res = context.getResources();
        if (knowledgeLevel < 1)
            return res.getString(R.string.knowledgeLevel1);
        else if (knowledgeLevel < 2)
            return res.getString(R.string.knowledgeLevel2);
        else if (knowledgeLevel < 3)
            return res.getString(R.string.knowledgeLevel3);
        else if (knowledgeLevel < 4)
            return res.getString(R.string.knowledgeLevel4);
        else
            return res.getString(R.string.knowledgeLevel5);
    }


    /**
     * Calculates a new KnowledgeLevel depending on whether it should be increased or decreased.
     *
     * @param oldLevel      The last day which serves as ase
     * @param increaseLevel if true the day is incremented, if false decremented.
     * @return The updated KnowledgeLevel
     */
    public static double calculateKnowledgeLevelAdjustment(double oldLevel, boolean increaseLevel) {
        double newLevel;
        // Increments/Decrements could be changed to a more dynamic approach (e.g. more or less
        // or depending on how often a word was trained.
        //TODO: adapt knowledge Level based on chosen preference instead of fixed 0.5
        Context context = MyApplication.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context);

        if (increaseLevel) {
            newLevel = oldLevel + Double.parseDouble(sharedPreferences.getString(context
                    .getString(R.string.settings_reward_correct_training_key), context.getString
                    (R.string.settings_adapt_training_medium)));
            if (newLevel > LEVEL_MAX)
                newLevel = LEVEL_MAX;

        } else {
            newLevel = oldLevel - Double.parseDouble(sharedPreferences.getString(context
                    .getString(R.string.settings_punish_wrong_training_key), context.getString(R
                    .string.settings_adapt_training_medium)));
            if (newLevel < LEVEL_MIN)
                newLevel = LEVEL_MIN;
        }
        newLevel = Math.round(newLevel * 100D) / 100D;
        Toast.makeText(context, "" + oldLevel + " -> " + newLevel, Toast.LENGTH_SHORT).show();

        return newLevel;
    }

}
