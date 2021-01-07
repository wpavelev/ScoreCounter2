package de.wpavelev.scorecounter2.dialogs;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.scorecounter2.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.wpavelev.scorecounter2.model.data.PlayerWithScore;
import de.wpavelev.scorecounter2.model.data.Score;

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
        LineChart chart = view.findViewById(R.id.chart);

        /*
        * https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/CombinedChartActivity.java
        * Combined Datasets ( für den Verlauf und Qwirkle anzeige)
        *
        * */
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        int i = 0;
        for (PlayerWithScore playerWithScore : mPlayerWithScoreList) {
            LineDataSet temp = new LineDataSet(convertData(playerWithScore.mPlayerScores), playerWithScore.mPlayer.getName());
            temp.setColor(playerColor(i));
            i++;
            temp.setLineWidth(5);
            temp.setDrawCircles(false);
            lineDataSets.add(temp);

        }


        LineData lineData = new LineData(lineDataSets);
        lineData.setValueTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.transparent));
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setData(lineData);

        chart.animateY(700);
        chart.invalidate();
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
