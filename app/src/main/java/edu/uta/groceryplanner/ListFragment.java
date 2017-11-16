package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
public class ListFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FragmentTabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null)
            startActivity(new Intent(getContext(),LoginActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_list);
        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        Bundle arg2 = new Bundle();
        arg1.putInt("Arg for Frag2", 2);
        Bundle arg3 = new Bundle();
        arg1.putInt("Arg for Frag3", 3);
        mTabHost.addTab(mTabHost.newTabSpec("draft").setIndicator("Draft"),DraftFragment.class,arg1);
        mTabHost.addTab(mTabHost.newTabSpec("ready").setIndicator("Ready"),ReadyFragment.class,arg2);
        mTabHost.addTab(mTabHost.newTabSpec("completed").setIndicator("Completed"),CompletedFragment.class,arg3);
        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
