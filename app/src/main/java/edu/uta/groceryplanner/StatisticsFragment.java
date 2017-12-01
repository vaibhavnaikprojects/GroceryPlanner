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
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uta.groceryplanner.adapters.InfoAdapter;
import edu.uta.groceryplanner.adapters.StatisticsAdapter;
import edu.uta.groceryplanner.beans.InfoItemBean;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.PredefinedCategory;
import edu.uta.groceryplanner.beans.ProductBean;
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
    private PieChart pieChart;
    private List<PieEntry> entry;
    private Map<String,Integer> productCount;
    private Map<String,Double> productCost;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private TextView textViewMonthYear;
    private DateTime date;
    private String currentUser;
    private DatabaseReference productRef,listRef;
    private FirebaseAuth firebaseAuth;
    private String listId;
    private String productTypeId;
    private List<PredefinedCategory> productCategoryList;
    private Double totalCost = 0.0;
    private Long totalCategoryCount = 0L;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

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

        listRef = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseAuth.getCurrentUser().getUid());
        entry = new ArrayList<>();
        productCount=new HashMap<>();
        productCost=new HashMap<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics,container, false);
        recyclerView=view.findViewById(R.id.statisticsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        statisticsBeanList=new ArrayList<>();

        date = new DateTime();
        pieChart = view.findViewById(R.id.pieChart);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        textViewMonthYear = view.findViewById(R.id.textViewMonthYear);
        textViewMonthYear.setText(date.monthOfYear().getAsText()+" "+date.year().getAsText());

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
        setPiechartData(10, 100);
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


    @Override
    public void onStart() {
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productCost.clear();
                productCount.clear();
                for(DataSnapshot datasnap: dataSnapshot.getChildren()){
                    final ListBean listBean = datasnap.getValue(ListBean.class);
                    productRef = FirebaseDatabase.getInstance().getReference("Products").child(listBean.getListId());
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double totalCost = 0.0;
                            Long totalCount = dataSnapshot.getChildrenCount();
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                ProductBean productBean = dataSnapshot1.getValue(ProductBean.class);
                                String category = productBean.getProductTypeId();
                                double cost = productBean.getCost();
                                totalCost += cost;
                                productCost.put(category, productCost.get(category)==null?cost:productCost.get(category)+ cost);
                                productCount.put(category, productCount.get(category)==null?1:productCount.get(category) + 1);
                            }

                            for(Map.Entry<String,Double> costEntry: productCost.entrySet()){

                                for(Map.Entry<String,Integer> countEntry: productCount.entrySet()) {
                                 if(countEntry.getKey().equals(costEntry.getKey())) {
                                     statisticsBeanList.add(new StatisticsBean(costEntry.getKey(),costEntry.getValue(), ((countEntry.getValue().doubleValue()*100)/totalCount)));
                                     entry.add(new PieEntry(Float.valueOf(Double.toString((costEntry.getValue()/totalCost)*100)),costEntry.getKey()));

                                 }
                                }
                            }
                            setUpPieChart();
                            adapter=new StatisticsAdapter(statisticsBeanList,getContext(),firebaseAuth);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onStart();
    }

    private void setPiechartData(int count, float range) {

        float mult = range;

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
        pieChart.notifyDataSetChanged();
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
