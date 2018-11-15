package com.sliebald.cula.ui.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.databinding.FragmentLibraryBinding;
import com.sliebald.cula.ui.updateLibrary.UpdateLibraryActivity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment presenting a list of {@link LibraryEntry}s and the possibility to add new ones.
 */
public class LibraryFragment extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        LibraryRecyclerViewAdapter.OnItemClickListener {

    public static final String TAG = LibraryFragment.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;
    private FragmentLibraryBinding mBinding;

    private LibraryViewModel mViewModel;

    private LibraryRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LibraryFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null || getContext() == null)
            return;
        mViewModel = ViewModelProviders.of(getActivity()).get(LibraryViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getLibraryEntries().observe(this, libraryEntries -> {
            if (libraryEntries == null || libraryEntries.size() == 0) {
                Snackbar snackbar = Snackbar
                        .make(mBinding.libraryCoordinatorLayout, R.string.library_add_first_word,
                                Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.add, (View view) -> updateLibraryActivity());
                snackbar.show();
            }
            mAdapter.swapEntries(libraryEntries);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            mBinding.recyclerViewLibraryList.smoothScrollToPosition(mPosition);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize Data Binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_library, container, false);

        // Set the adapter
        Context context = mBinding.recyclerViewLibraryList.getContext();
        mBinding.recyclerViewLibraryList.setLayoutManager(new LinearLayoutManager(context));

        if (getContext() == null)
            return mBinding.getRoot();

        mAdapter = new LibraryRecyclerViewAdapter(this, getContext());
        mBinding.recyclerViewLibraryList.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBinding
                .recyclerViewLibraryList);

        //Set the setOnClickListener for the Floating Action Button
        mBinding.fabAddWord.setOnClickListener(v -> updateLibraryActivity());
        mBinding.fabAddWord.setImageDrawable(new IconicsDrawable(getContext()).icon(FontAwesome
                .Icon.faw_plus).color(Color.WHITE).sizeDp(24));

        return mBinding.getRoot();
    }

    /**
     * Called when the recycler view is swiped.
     * The swiped item  will be removed with an undo option.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof LibraryRecyclerViewAdapter.ViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from the viewModel
            mViewModel.removeLibraryEntry(deletedIndex);
            // show undo option
            Snackbar snackbar = Snackbar
                    .make(mBinding.libraryCoordinatorLayout, R.string.library_word_deleted,
                            Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, (View view) -> {

                mViewModel.restoreLatestDeletedLibraryEntry();
                Snackbar.make(mBinding.libraryCoordinatorLayout, R.string.restored, Snackbar
                        .LENGTH_SHORT).show();
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void updateLibraryActivity() {
        Intent intent = new Intent(getContext(), UpdateLibraryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLibraryEntryClick(int id) {
        Intent intent = new Intent(getContext(), UpdateLibraryActivity.class);
        intent.putExtra(UpdateLibraryActivity.BUNDLE_EXTRA_UPDATE_KEY, id);
        startActivity(intent);
    }
}
