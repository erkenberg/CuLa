package com.sliebald.cula.utilities;

public class SortUtils {

    public static final String KEY_ACTIVE_SORT_BY = "KEY_ACTIVE_SORT_BY";
    public static final String KEY_ACTIVE_SORT_ORDER = "KEY_ACTIVE_SORT_ORDER";

    public enum SortType {KNOWLEDGE_LEVEL, NATIVE_WORD, FOREIGN_WORD, ID, NAME,PART_OF_LESSON}

    public interface OnSortChangedListener {
        void onUpdateSortOrderClick(SortUtils.SortType type, boolean asc);
    }
}
