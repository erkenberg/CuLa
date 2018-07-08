package org.liebald.android.cula.ui.lessons;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.database.Entities.LessonEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a list of {@link LessonEntry}s.
 */
public class LessonsRecyclerViewAdapter extends
        RecyclerView.Adapter<LessonsRecyclerViewAdapter.ViewHolder> {

    private final OnItemClickListener mListener;

    private List<LessonEntry> mValues;

    LessonsRecyclerViewAdapter(OnItemClickListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mLessonName.setText(mValues.get(position).getLessonName());
        holder.mLessonDescription.setText(mValues.get(position).getLessonDescription());
    }

    void swapEntries(final List<LessonEntry> newLessonEntries) {
        if (mValues == null) {
            mValues = newLessonEntries;
            notifyDataSetChanged();
            Log.d("adapter", "adapter called, notified changed");

        } else {
            /*
             * Use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mValues, where the new list is the new
             * values passed in from the observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mValues.size();
                }

                @Override
                public int getNewListSize() {
                    return newLessonEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getId() ==
                            newLessonEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    LessonEntry newEntry = newLessonEntries.get(newItemPosition);
                    LessonEntry oldEntry = mValues.get(oldItemPosition);
                    return newEntry.getId() == oldEntry.getId()
                            && newEntry.getLanguage().equals(oldEntry.getLanguage())
                            && newEntry.getLessonName().equals(oldEntry.getLessonName())
                            && newEntry.getLessonDescription() == oldEntry.getLessonDescription();
                }
            });
            mValues = newLessonEntries;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        return mValues.size();
    }

    public interface OnItemClickListener {
        void onLessonEntryClick(View view, int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mLessonName;
        final TextView mLessonDescription;
        final LinearLayout viewForeground;
        final RelativeLayout viewBackground;

        ViewHolder(View view) {
            super(view);
            mLessonName = view.findViewById(R.id.lesson_name);
            mLessonDescription = view.findViewById(R.id.lesson_description);
            viewForeground = view.findViewById(R.id.view_foreground);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground.setOnClickListener(v -> mListener.onLessonEntryClick(v, mValues.get(getAdapterPosition()).getId()));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLessonName.getText() + "'" + " '" + mLessonDescription
                    .getText() + "'";
        }
    }


}
