package com.sliebald.cula.ui.library;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.databinding.FragmentLibraryBinding;
import com.sliebald.cula.ui.updateLibrary.UpdateLibraryActivity;

/**
 * A fragment presenting a list of {@link LibraryEntry}s and the possibility to add new ones.
 */
public class LibraryFragment extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        LibraryRecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = LibraryFragment.class.getSimpleName();
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

        mViewModel.getLibraryEntries().observe(this, libraryEntries -> {
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

        mBinding.recyclerViewLibraryList.addItemDecoration(new DividerItemDecoration(getContext()
                , DividerItemDecoration.VERTICAL));
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LibraryRecyclerViewAdapter.ViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from the viewModel
            mViewModel.removeLibraryEntry(deletedIndex);
            // show undo option
            Snackbar snackbar = Snackbar
                    .make(mBinding.libraryCoordinatorLayout, R.string.word_removed_from_library,
                            Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, (View view) -> {
                Toast.makeText(getContext(), R.string.restored, Toast.LENGTH_SHORT).show();
                mViewModel.restoreLatestDeletedLibraryEntry();
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    void updateLibraryActivity() {
        Intent intent = new Intent(getContext(), UpdateLibraryActivity.class);
        // intent.putExtra(UpdateLibraryActivity.BUNDLE_EXTRA_UPDATE_KEY, 1);
        startActivity(intent);
    }

    @Override
    public void onLibraryEntryClick(View view, int id) {
        Intent intent = new Intent(getContext(), UpdateLibraryActivity.class);
        intent.putExtra(UpdateLibraryActivity.BUNDLE_EXTRA_UPDATE_KEY, id);
        startActivity(intent);
    }
}