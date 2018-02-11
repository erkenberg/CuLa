package com.liebald.android.cula.ui.updateDictionary;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.liebald.android.cula.R;
import com.liebald.android.cula.databinding.FragmentUpdateDictionaryBinding;
import com.liebald.android.cula.utilities.InjectorUtils;

/**
 * A fragment representing a list of DictionaryEntries and the possibility to add new ones.
 */
public class UpdateDictionaryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private int mPosition = RecyclerView.NO_POSITION;
    private FragmentUpdateDictionaryBinding mBinding;

    private UpdateDictionaryFragmentViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UpdateDictionaryFragment() {

    }

    // TODO: Customize parameter initialization
    //public static UpdateDictionaryFragment newInstance(int columnCount) {
    //   UpdateDictionaryFragment fragment = new UpdateDictionaryFragment();
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
        UpdateDictionaryViewModelFactory factory = InjectorUtils.provideUpdateDictionaryViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(UpdateDictionaryFragmentViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize Data Binding

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_dictionary, container, false);
        View view = mBinding.getRoot();
        mBinding.setModel(mViewModel);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_update_dictionary_list);
        // Set the adapter
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        Log.d("test", "attached adapter");
        UpdateDictionaryFragmentRecyclerViewAdapter adapter = new UpdateDictionaryFragmentRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        Log.d("test2", "" + mViewModel.getDictionaryEntries());
        mViewModel.getDictionaryEntries().observe(this, dictionaryEntries -> {
            adapter.swapForecast(dictionaryEntries);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            recyclerView.smoothScrollToPosition(mPosition);
        });

        mBinding.editTextAddForeignWord.setOnKeyListener((View view2, int key, KeyEvent keyEvent) -> {
            if (keyEvent.getAction() == keyEvent.ACTION_UP && key == KeyEvent.KEYCODE_ENTER) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                mBinding.editTextAddForeignWord.clearFocus();
                return true;
            }
            return false;
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
