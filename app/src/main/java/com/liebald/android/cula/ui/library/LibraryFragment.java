package com.liebald.android.cula.ui.library;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liebald.android.cula.R;
import com.liebald.android.cula.databinding.FragmentUpdateLibraryBinding;
import com.liebald.android.cula.utilities.InjectorUtils;

/**
 * A fragment presenting a list of {@link com.liebald.android.cula.data.database.LibraryEntry}s and the possibility to add new ones.
 */
public class LibraryFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private int mPosition = RecyclerView.NO_POSITION;
    private FragmentUpdateLibraryBinding mBinding;

    private LibraryFragmentViewModel mViewModel;

    private LibraryFragmentRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LibraryFragment() {

    }
    // TODO: Customize parameter initialization
    //public static LibraryFragment newInstance(int columnCount) {
    //   LibraryFragment fragment = new LibraryFragment();
    //   Bundle args = new Bundle();
    //   args.putInt(ARG_COLUMN_COUNT, columnCount);
    //   fragment.setArguments(args);
    //    return fragment;
    // }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        if (getActivity() == null || getContext() == null)
            return;
        LibraryViewModelFactory factory = InjectorUtils.provideLibraryViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(LibraryFragmentViewModel.class);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize Data Binding

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_library, container, false);
        View view = mBinding.getRoot();
        mBinding.setModel(mViewModel);

        // Set the adapter
        Context context = mBinding.recyclerViewLibraryList.getContext();
        if (mColumnCount <= 1) {
            mBinding.recyclerViewLibraryList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mBinding.recyclerViewLibraryList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        Log.d("test", "attached adapter");
        if(getContext()==null)
            return view;
        mBinding.recyclerViewLibraryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new LibraryFragmentRecyclerViewAdapter();
        mBinding.recyclerViewLibraryList.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBinding.recyclerViewLibraryList);


        Log.d("test2", "" + mViewModel.getLibraryEntries());
        mViewModel.getLibraryEntries().observe(this, libraryEntries -> {
            mAdapter.swapForecast(libraryEntries);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            mBinding.recyclerViewLibraryList.smoothScrollToPosition(mPosition);
        });

        mBinding.fabAddWord.setOnClickListener(v -> Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show());

//        mBinding.editTextAddForeignWord.setOnKeyListener((View view2, int key, KeyEvent keyEvent) -> {
//            if (keyEvent.getAction() == KeyEvent.ACTION_UP && key == KeyEvent.KEYCODE_ENTER) {
//                if (getActivity() == null)
//                    return false;
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    mBinding.editTextAddForeignWord.clearFocus();
//                }
//                return true;
//            }
//            return false;
//        });
        return view;
    }

    /**
     * Called when the recycler view is swiped.
     * The swiped item  will be removed with an undo option.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LibraryFragmentRecyclerViewAdapter.ViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from the viewModel
            mViewModel.removeLibraryEntry(deletedIndex);
            // show undo option
            Snackbar snackbar = Snackbar
                    .make(mBinding.libraryCoordinatorLayout, R.string.word_removed_from_library, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, (View view) -> {
                Toast.makeText(getContext(), R.string.restored, Toast.LENGTH_SHORT).show();
                mViewModel.restoreLatestDeletedLibraryEntry();
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

}
