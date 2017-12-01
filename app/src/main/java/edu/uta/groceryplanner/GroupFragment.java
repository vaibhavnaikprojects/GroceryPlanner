package edu.uta.groceryplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab,groupFab,billFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    DatabaseReference groupsRef;


    public GroupFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
        groupsRef = FirebaseDatabase.getInstance().getReference("groups").child(firebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        mainFab = view.findViewById(R.id.mainFab);
        groupFab = view.findViewById(R.id.groupFab);
        billFab = view.findViewById(R.id.billFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        groupFab.setOnClickListener(this);
        billFab.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.mainFab:
                animateFAB();
                break;
            case R.id.groupFab:
                startActivity(new Intent(getContext(),GroupActivity.class));
                break;
            case R.id.billFab:
                startActivity(new Intent(getContext(), AddBillActivity.class));
                break;
        }
    }
    public void animateFAB() {

        if (isFabOpen) {
            mainFab.startAnimation(rotate_backward);
            groupFab.startAnimation(fab_close);
            billFab.startAnimation(fab_close);
            groupFab.setClickable(false);
            billFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotate_forward);
            groupFab.startAnimation(fab_open);
            billFab.startAnimation(fab_open);
            groupFab.setClickable(true);
            billFab.setClickable(true);
            isFabOpen = true;
        }
    }
}
