package com.sliebald.cula.ui.updateLesson;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Pojos.MappingPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MappingPOJO}.
 */
public class UpdateLessonRecyclerViewAdapter extends
        RecyclerView.Adapter<UpdateLessonRecyclerViewAdapter.ViewHolder> {

    private final UpdateLessonRecyclerViewAdapter.OnItemClickListener mListener;

    private List<MappingPOJO> mValues;

    UpdateLessonRecyclerViewAdapter(UpdateLessonRecyclerViewAdapter.OnItemClickListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public UpdateLessonRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                                 parent, int
                                                                                 viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_mapping_item, parent, false);
        return new UpdateLessonRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpdateLessonRecyclerViewAdapter.ViewHolder
                                         holder, int position) {
        holder.mNativeWord.setText(mValues.get(position).getNative_word());
        holder.mForeignWord.setText(mValues.get(position).getForeign_word());
        holder.mPartOfLesson.setChecked(mValues.get(position).partOfLesson);
    }

    void swapEntries(final List<MappingPOJO> mapping) {
        if (mValues == null) {
            mValues = mapping;
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
                    return mapping.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getLibraryId() ==
                            mapping.get(newItemPosition).getLibraryId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MappingPOJO newEntry = mapping.get(newItemPosition);
                    MappingPOJO oldEntry = mValues.get(oldItemPosition);
                    return newEntry.getLibraryId() == oldEntry.getLibraryId()
                            && newEntry.getForeign_word().equals(oldEntry.getForeign_word())
                            && newEntry.getNative_word().equals(oldEntry.getNative_word())
                            && newEntry.isPartOfLesson() == oldEntry.isPartOfLesson();
                }
            });
            mValues = mapping;
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
        void onLessonEntryClick(CheckBox checkBox, int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox mPartOfLesson;
        final TextView mNativeWord;
        final TextView mForeignWord;

        ViewHolder(View view) {
            super(view);
            mPartOfLesson = view.findViewById(R.id.cb_lesson_mapping);
            mNativeWord = view.findViewById(R.id.tv_lesson_mapping_native_word);
            mForeignWord = view.findViewById(R.id.tv_lesson_mapping_foreign_word);
            mPartOfLesson.setOnClickListener(v -> mListener.onLessonEntryClick(mPartOfLesson,
                    mValues.get(getAdapterPosition()).getLibraryId()));
        }

    }


}
