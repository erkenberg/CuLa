package org.liebald.android.cula.ui.quote;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liebald.android.cula.R;
import org.liebald.android.cula.databinding.FragmentQuoteBinding;
import org.liebald.android.cula.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteFragment extends Fragment {

    private FragmentQuoteBinding mBinding;

    private QuoteViewModel mViewModel;


    public QuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null || getContext() == null)
            return;
        QuoteViewModelFactory factory = InjectorUtils.provideQuoteViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(QuoteViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quote, container, false);

        mViewModel.getQuote().observe(this, quote -> {
            if (quote != null)
                mBinding.quoteText.setText(quote.getText());
        });

        return mBinding.getRoot();
    }

}
