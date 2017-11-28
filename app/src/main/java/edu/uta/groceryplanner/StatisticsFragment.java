package edu.uta.groceryplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.InfoAdapter;
import edu.uta.groceryplanner.adapters.StatisticsAdapter;
import edu.uta.groceryplanner.beans.InfoItemBean;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.StatisticsBean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StatisticsBean> statisticsBeanList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnChooseDate;
    private PieChart pieChart;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private TextView textViewMonthYear;
    private DateTime date;
    private FirebaseAuth firebaseAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        recyclerView=view.findViewById(R.id.statisticsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        statisticsBeanList=new ArrayList<>();
        statisticsBeanList.add(new StatisticsBean("Veggies",50,50));
        statisticsBeanList.add(new StatisticsBean("Dairy",60,40));
        statisticsBeanList.add(new StatisticsBean("Drinks",90,12));
        statisticsBeanList.add(new StatisticsBean("Cosmetics",50,50));
        statisticsBeanList.add(new StatisticsBean("Meat",80,20));
        statisticsBeanList.add(new StatisticsBean("Utensils",50,55));
        statisticsBeanList.add(new StatisticsBean("Tissue Paper",70,78));
        adapter=new StatisticsAdapter(statisticsBeanList,getContext(),firebaseAuth);
        recyclerView.setAdapter(adapter);

        date = new DateTime();
        pieChart = view.findViewById(R.id.pieChart);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        textViewMonthYear = view.findViewById(R.id.textViewMonthYear);
        textViewMonthYear.setText(date.monthOfYear().getAsText()+" "+date.year().getAsText());
        setUpPieChart();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNextMonth();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPreviousMonth();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void setUpPieChart(){
        pieChart.setDrawCenterText(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterTextTypeface(Typeface.SANS_SERIF);
        pieChart.setCenterText("Monthly");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        setPiechartData(4, 100);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(Typeface.SANS_SERIF);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void setPiechartData(int count, float range) {

        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        List<PieEntry> entry = new ArrayList<PieEntry>();
        entry.add(new PieEntry(50,"Veggies"));
        entry.add(new PieEntry(10,"Drinks"));
        entry.add(new PieEntry(10,"Dairy"));
        entry.add(new PieEntry(20,"Meat"));
        entry.add(new PieEntry(10,"Cosmetics"));

        PieDataSet dataSet = new PieDataSet(entry, "User Monthly Stats");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(2f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.SANS_SERIF);
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //get the next month when user clicks on the next button
    public void getNextMonth(){
        DateTime nextMonthDate = date.plusMonths(1);
        textViewMonthYear.setText(nextMonthDate.monthOfYear().getAsText()+" "+nextMonthDate.year().getAsText());
        date = nextMonthDate;
    }

    //get the next month when user clicks on the next button
    public void getPreviousMonth(){
        DateTime previousMonthDate = date.minusMonths(1);
        textViewMonthYear.setText(previousMonthDate.monthOfYear().getAsText()+" "+previousMonthDate.year().getAsText());
        date = previousMonthDate;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
