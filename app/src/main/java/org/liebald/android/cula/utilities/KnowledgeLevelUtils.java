package org.liebald.android.cula.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;

import com.liebald.android.cula.R;

public class KnowledgeLevelUtils {


    public static int getColorByKnowledgeLevel(Context context, double knowledgeLevel) {
        Resources res = context.getResources();
        if (knowledgeLevel < 1.5)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_1, null);
        else if (knowledgeLevel < 2.5)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_2, null);
        else if (knowledgeLevel < 3.5)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_3, null);
        else if (knowledgeLevel < 4.5)
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_4, null);
        else
            return ResourcesCompat.getColor(res, R.color.knowledgeLevel_5, null);
    }
}
