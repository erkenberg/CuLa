package com.sliebald.cula.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sliebald.cula.MyApplication;
import com.sliebald.cula.R;

public class PreferenceUtils {

    /**
     * Returns by how much the Knowledgelevel of a correctly trained word should be incremented.
     *
     * @return Double that should be added to the Knowledgelevel of the trained word.
     */
    public static double getKnowledgeIncrementCorrectTraining() {
        Context context = MyApplication.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.
                settings_reward_correct_training_key), 5) / 10D;
    }

    /**
     * Returns by how much the Knowledgelevel of a wrongly trained word should be decremented.
     *
     * @return Double that should be removed from the Knowledgelevel of the trained word.
     */
    public static double getKnowledgeDecrementWrongTraining() {
        Context context = MyApplication.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.
                settings_punish_wrong_training_key), 5) / 10D;
    }

    /**
     * Returns the currently active language.
     *
     * @return The currently active language.
     */
    public static String getActiveLanguage() {
        Context context = MyApplication.getContext();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString
                (R.string.settings_select_language_key), "");
    }

}
