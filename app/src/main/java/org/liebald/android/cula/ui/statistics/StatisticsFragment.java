package org.liebald.android.cula.ui.statistics;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.database.Pojos.StatisticsLibraryWordCount;
import org.liebald.android.cula.databinding.FragmentStatisticsBinding;
import org.liebald.android.cula.utilities.InjectorUtils;
import org.liebald.android.cula.utilities.KnowledgeLevelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding mBinding;

    private StatisticsViewModel mViewModel;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null || getContext() == null)
            return;
        StatisticsViewModelFactory factory = InjectorUtils.provideStatisticsViewModelFactory
                (getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(StatisticsViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container,
                false);

        mViewModel.getLibraryCount().observe(this, libraryWordCountList -> {
            if (libraryWordCountList != null && libraryWordCountList.size() > 0) {
                addDataSet(libraryWordCountList);
            }
        });

        return mBinding.getRoot();
    }

    private void addDataSet(List<StatisticsLibraryWordCount> libraryWordCountList) {
        Log.d("test", "test " + libraryWordCountList.size());
        ArrayList<PieEntry> counts = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (StatisticsLibraryWordCount entry : libraryWordCountList) {
            counts.add(new PieEntry((float) entry
                    .getCount(), entry.getLevel()));
            colors.add(KnowledgeLevelUtils.getColorByKnowledgeLevel(Objects.requireNonNull
                    (getContext()), entry
                    .level));
        }


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(counts, "");
        pieDataSet.setValueTextSize(24);

        pieDataSet.setColors(colors);


        IValueFormatter formatter = (value, entry, dataSetIndex, viewPortHandler) -> Integer
                .toString((int) value);

        pieDataSet.setValueFormatter(formatter);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mBinding.chart.setHoleRadius(0);
        mBinding.chart.setTransparentCircleRadius(0);
        mBinding.chart.setData(pieData);
        mBinding.chart.getDescription().setEnabled(false);
        mBinding.chart.getLegend().setEnabled(false);
        mBinding.chart.invalidate();
    }

}
