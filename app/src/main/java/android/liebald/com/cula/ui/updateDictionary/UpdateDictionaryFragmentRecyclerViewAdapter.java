package android.liebald.com.cula.ui.updateDictionary;

import android.liebald.com.cula.R;
import android.liebald.com.cula.data.database.DictionaryEntry;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DictionaryEntry}.
 */
public class UpdateDictionaryFragmentRecyclerViewAdapter extends
        RecyclerView.Adapter<UpdateDictionaryFragmentRecyclerViewAdapter.ViewHolder> {

    private List<DictionaryEntry> mValues;

    public UpdateDictionaryFragmentRecyclerViewAdapter() {
        mValues = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dictionaryentrylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNativeWordView.setText(mValues.get(position).getForeignWord());
        holder.mForeignWordView.setText(mValues.get(position).getForeignWord());

    }

    void swapForecast(final List<DictionaryEntry> newDictionaryEntries) {
        // If there was no forecast data, then recreate all of the list
        if (mValues == null) {
            mValues = newDictionaryEntries;
            notifyDataSetChanged();
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
                    return newDictionaryEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getId() ==
                            newDictionaryEntries.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DictionaryEntry newEntry = newDictionaryEntries.get(newItemPosition);
                    DictionaryEntry oldEntry = mValues.get(oldItemPosition);
                    return newEntry.getId() == oldEntry.getId()
                            && newEntry.getForeignWord().equals(oldEntry.getForeignWord()) && newEntry.getNativeWord().equals(oldEntry.getNativeWord());
                }
            });
            mValues = newDictionaryEntries;
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

        final View mView;
        final TextView mNativeWordView;
        final TextView mForeignWordView;
        DictionaryEntry mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mNativeWordView = view.findViewById(R.id.nativeWord);
            mForeignWordView = view.findViewById(R.id.foreignWord);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNativeWordView.getText() + "'" + " '" + mForeignWordView
                    .getText() + "'";
        }
    }
}
