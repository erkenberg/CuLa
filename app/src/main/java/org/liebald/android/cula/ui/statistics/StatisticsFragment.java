package org.liebald.android.cula.ui.statistics;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.database.Pojos.StatisticsActivityEntry;
import org.liebald.android.cula.data.database.Pojos.StatisticsLibraryWordCount;
import org.liebald.android.cula.databinding.FragmentStatisticsBinding;
import org.liebald.android.cula.utilities.KnowledgeLevelUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class StatisticsFragment extends Fragment {

    private static final String TAG = StatisticsFragment.class.getSimpleName();
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

        mViewModel = ViewModelProviders.of(getActivity()).get(StatisticsViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container,
                false);

        mViewModel.getLibraryCount().observe(this, libraryWordCountList -> {
            if (libraryWordCountList != null && libraryWordCountList.size() > 0) {
                updateWordCountGraph(libraryWordCountList);
            }
        });

        mViewModel.getActivity().observe(this, activityList -> {
            Log.d(TAG, "" + activityList);
            if (activityList != null && activityList.size() > 0) {
                updateActivityGraph(activityList);

            }
        });


        return mBinding.getRoot();
    }


    private void updateActivityGraph(List<StatisticsActivityEntry> activityList) {

        LineChart mChart = mBinding.chartActivity;
        // disable description and legend
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        // disable touch gestures
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        // set the axis parameters
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelRotationAngle(310);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setLabelCount(6, false);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(14f);

        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);

        // add data
        ArrayList<Entry> yVals = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);


        for (StatisticsActivityEntry entry : activityList) {

            Date date;
            try {
                date = format.parse(entry.getDate());
                System.out.println(date);
            } catch (ParseException e) {
                Log.d(TAG, "Exception parsing a date from the database: " + e);
                continue;
            }

            yVals.add(new Entry(date.getTime(), entry.getActivity()));
        }

        xAxis.setValueFormatter((value, axis) -> new SimpleDateFormat("dd.MM.", Locale.GERMANY)
                .format(new Date(((long)
                value))));
        xAxis.setLabelCount(Math.min(7, yVals.size()), true);

        Log.d(TAG, "" + yVals);

        int color = ResourcesCompat.getColor(getResources(), R.color.secondaryColor, null);
        LineDataSet lineDataSet;

        lineDataSet = new LineDataSet(yVals, "");

        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
//        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(100);

        // create a data object with the dataset
        LineData data = new LineData(lineDataSet);
        data.setDrawValues(false);

        // set data
        mChart.setData(data);

        mChart.animateXY(1000, 1000);

        // dont forget to refresh the drawing
        mChart.invalidate();

    }


    private void updateWordCountGraph(List<StatisticsLibraryWordCount> libraryWordCountList) {
        ArrayList<PieEntry> counts = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        Context context = Objects.requireNonNull(getContext());

        for (StatisticsLibraryWordCount entry : libraryWordCountList) {
            counts.add(new PieEntry((float) entry.getCount(), KnowledgeLevelUtils
                    .getNameByKnowledgeLevel(context, entry.level)));
            colors.add(KnowledgeLevelUtils.getColorByKnowledgeLevel(context, entry.level));

        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(counts, "");
        pieDataSet.setValueTextSize(22);
        pieDataSet.setColors(colors);

        //Set the formatter to not display floats but integers.
        IValueFormatter formatter = (value, entry, dataSetIndex, viewPortHandler) -> Integer
                .toString((int) value);
        pieDataSet.setValueFormatter(formatter);

        Legend legend = mBinding.chartWordCount.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(18);
        legend.setFormSize(18);
        legend.setWordWrapEnabled(true);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mBinding.chartWordCount.setHoleRadius(0);

        mBinding.chartWordCount.setTransparentCircleRadius(0);
        mBinding.chartWordCount.setDrawEntryLabels(false);
        mBinding.chartWordCount.setData(pieData);
        mBinding.chartWordCount.getDescription().setEnabled(false);
        mBinding.chartWordCount.getLegend().setEnabled(true);
        mBinding.chartWordCount.invalidate();
    }

}
