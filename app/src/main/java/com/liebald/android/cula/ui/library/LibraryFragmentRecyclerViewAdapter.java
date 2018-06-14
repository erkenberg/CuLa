package com.liebald.android.cula.ui.library;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liebald.android.cula.R;
import com.liebald.android.cula.data.database.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LibraryEntry}.
 */
public class LibraryFragmentRecyclerViewAdapter extends
        RecyclerView.Adapter<LibraryFragmentRecyclerViewAdapter.ViewHolder> {

    private List<LibraryEntry> mValues;

    LibraryFragmentRecyclerViewAdapter() {
        mValues = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.update_library_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mNativeWordView.setText(mValues.get(position).getNativeWord());
        holder.mForeignWordView.setText(mValues.get(position).getForeignWord());
    }

    void swapForecast(final List<LibraryEntry> newLibraryEntries) {
        // If there was no forecast data, then recreate all of the list
        if (mValues == null) {
            mValues = newLibraryEntries;
            notifyDataSetChanged();
            Log.d("adapter", "adapter called, notified changed");

        } else {
            /*
            * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
            * shows the four methods you need to override to return a DiffUtil callback. The
            * old list is the current list stored in mForecast, where the new list is the new
            * values passed in from the observing the database.
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
                    return newEntry.getId() == oldEntry.getId()
                            && newEntry.getForeignWord().equals(oldEntry.getForeignWord()) && newEntry.getNativeWord().equals(oldEntry.getNativeWord());
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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNativeWordView.getText() + "'" + " '" + mForeignWordView
                    .getText() + "'";
        }
    }
}
