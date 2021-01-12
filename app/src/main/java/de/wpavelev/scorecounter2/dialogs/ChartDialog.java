package de.wpavelev.scorecounter2.dialogs;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.scorecounter2.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.data.Score;

import static com.example.scorecounter2.R.drawable.color_player_0;

public class ChartDialog extends DialogFragment {

    List<PlayerWithScore> mPlayerWithScoreList;


    public ChartDialog(List<PlayerWithScore> data) {
        this.mPlayerWithScoreList = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_chart, null);

        setupChart(view);


        builder.setView(view).setTitle(R.string.chart_dialog_title)

                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());


        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    private void setupChart(View view) {
        CombinedChart chart = view.findViewById(R.id.chart);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE,
                CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER});
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.animateY(700);


        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)



        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(-1);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(mPlayerWithScoreList.size());
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        List<IBarDataSet> barDataSets = new ArrayList<>();

        int maxQwirkle = 0;

        int i = 0;

        for (PlayerWithScore playerWithScore : mPlayerWithScoreList) {
            maxQwirkle = Math.max(maxQwirkle, playerWithScore.mPlayer.getQwirkle());

            BarEntry barEntry = new BarEntry(i, playerWithScore.mPlayer.getQwirkle());
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(barEntry);
            BarDataSet barDataSet = new BarDataSet(barEntries, playerWithScore.mPlayer.getName());
            barDataSet.setColor(playerColor(i));
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSets.add(barDataSet);


            LineDataSet lineDataSet = new LineDataSet(convertData(playerWithScore.mPlayerScores), playerWithScore.mPlayer.getName());
            lineDataSet.setColor(playerColor(i));
            lineDataSet.setLineWidth(4);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            lineDataSets.add(lineDataSet);
            i++;

        }

        for (ILineDataSet lineDataSet : lineDataSets) {
            lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        }

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(maxQwirkle+2);



        LineData lineData = new LineData(lineDataSets);
        lineData.setDrawValues(false);



        BarData barData = new BarData(barDataSets);

        barData.setDrawValues(false);



        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(barData);


        chart.setData(combinedData);
        chart.invalidate();
    }

    private String convertStringArrayToString(String[] array, String separation) {

        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
            sb.append(separation);
        }

        return sb.toString();
    }

    private List<Entry> convertData(List<Score> scores) {
        List<Entry> entrySet = new ArrayList<>();
        int subtotal = 0;
        for (int i = 0; i < scores.size(); i++) {
            subtotal += scores.get(i).getScore();
            entrySet.add(new Entry(i, subtotal));
        }
        return entrySet;
    }

    public int playerColor(int playerPosition) {

        TypedArray activePlayerArrayColor;

        activePlayerArrayColor = getResources().obtainTypedArray(R.array.player_color);

        int arrayPosition;

        arrayPosition = playerPosition % 4;

        int activePlayerColorId = activePlayerArrayColor.getResourceId(arrayPosition, 0);
        int playerBackground = ContextCompat.getColor(Objects.requireNonNull(getContext()), activePlayerColorId);

        activePlayerArrayColor.recycle();

        return playerBackground;




    }

}
