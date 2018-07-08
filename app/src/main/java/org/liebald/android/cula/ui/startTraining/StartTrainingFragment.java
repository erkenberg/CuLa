package org.liebald.android.cula.ui.startTraining;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liebald.android.cula.R;
import org.liebald.android.cula.databinding.FragmentStartTrainingBinding;
import org.liebald.android.cula.utilities.InjectorUtils;

/**
 * The fragment for starting a training session.
 */
public class StartTrainingFragment extends Fragment {

    /**
     * The Databinding for the layout.
     */
    private FragmentStartTrainingBinding mBinding;

    /**
     * The viewModel for the {@link StartTrainingFragment}.
     */
    private StartTrainingViewModel mViewModel;


    public StartTrainingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null || getContext() == null)
            return;
        // Create the ViewModel for this fragment.
        StartTrainingViewModelFactory factory = InjectorUtils.provideStartTrainingViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(StartTrainingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_training, container, false);

//        mViewModel.getQuote().observe(this, quote -> {
//            if (quote != null) {
//                mBinding.quoteText.setText(quote.getText());
//                mBinding.quoteAuthor.setText(quote.getAuthor());
//            }
//        });

        return mBinding.getRoot();
    }

    /**
     * Start the training Activity with correct settings.
     *
     * @param view The button that called this method.
     */
    public void startTraining(View view) {
    }
}
