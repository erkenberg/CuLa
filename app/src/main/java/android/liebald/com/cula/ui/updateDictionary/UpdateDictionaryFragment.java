package android.liebald.com.cula.ui.updateDictionary;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.liebald.com.cula.R;
import android.liebald.com.cula.utilities.InjectorUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of DictionaryEntries and the possibility to add new ones.
 */
public class UpdateDictionaryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private int mPosition = RecyclerView.NO_POSITION;


    private UpdateDictionaryFragmentViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UpdateDictionaryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UpdateDictionaryFragment newInstance(int columnCount) {
        UpdateDictionaryFragment fragment = new UpdateDictionaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_dictionaryentrylist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Log.d("test", "attached adapter");
            UpdateDictionaryFragmentRecyclerViewAdapter adapter = new UpdateDictionaryFragmentRecyclerViewAdapter();
            recyclerView.setAdapter(adapter);
            Log.d("test2",""+mViewModel.getDictionaryEntries());
            mViewModel.getDictionaryEntries().observe(this, dictionaryEntries -> {
                adapter.swapForecast(dictionaryEntries);
                if (mPosition == RecyclerView.NO_POSITION) {
                    mPosition = 0;
                }
                recyclerView.smoothScrollToPosition(mPosition);

            });

        }
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
