package com.sliebald.android.cula.ui.library;

import android.content.Context;
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

import com.sliebald.android.cula.R;
import com.sliebald.android.cula.data.database.Entities.LibraryEntry;
import com.sliebald.android.cula.utilities.KnowledgeLevelUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LibraryEntry}.
 */
public class LibraryRecyclerViewAdapter extends
        RecyclerView.Adapter<LibraryRecyclerViewAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    /**
     * Context required to set the correct colors for the day.
     */
    private final Context mContext;
    private List<LibraryEntry> mValues;

    LibraryRecyclerViewAdapter(OnItemClickListener listener, Context context) {
        mValues = new ArrayList<>();
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mNativeWordView.setText(mValues.get(position).getNativeWord());
        holder.mForeignWordView.setText(mValues.get(position).getForeignWord());
        holder.viewForeground.setBackgroundColor(KnowledgeLevelUtils.getColorByKnowledgeLevel
                (mContext, mValues.get(position).getKnowledgeLevel()));

    }

    void swapEntries(final List<LibraryEntry> newLibraryEntries) {
        if (mValues == null) {
            mValues = newLibraryEntries;
            notifyDataSetChanged();
            Log.d("adapter", "adapter called, notified changed");

        } else {
            /*
             * Use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mValues, where the new list is the new
             * values passed in from the observing the database.
             * Based on the Diffultil used in parts of the Udacity Android nanodegree course.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mValues.size();
                }

                @Override
                public int getNewListSize() {
                    return newLibraryEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getId() ==
                            newLibraryEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    LibraryEntry newEntry = newLibraryEntries.get(newItemPosition);
                    LibraryEntry oldEntry = mValues.get(oldItemPosition);
                    return newEntry.getId() == oldEntry.getId() && newEntry.getForeignWord()
                            .equals(oldEntry.getForeignWord()) && newEntry.getNativeWord().equals
                            (oldEntry.getNativeWord()) && newEntry.getKnowledgeLevel() ==
                            oldEntry.getKnowledgeLevel() && oldEntry.getLanguage().equals(newEntry
                            .getLanguage()) && oldEntry.getLastUpdated().equals(newEntry
                            .getLastUpdated());
                }
            });
            mValues = newLibraryEntries;
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
        void onLibraryEntryClick(View view, int id);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mNativeWordView;
        final TextView mForeignWordView;
        final LinearLayout viewForeground;
        final RelativeLayout viewBackground;

        ViewHolder(View view) {
            super(view);
            mNativeWordView = view.findViewById(R.id.nativeWord);
            mForeignWordView = view.findViewById(R.id.foreignWord);
            viewForeground = view.findViewById(R.id.view_foreground);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground.setOnClickListener(v -> mListener.onLibraryEntryClick(v, mValues.get
                    (getAdapterPosition()).getId()));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNativeWordView.getText() + "'" + " '" +
                    mForeignWordView
                    .getText() + "'";
        }
    }


}
