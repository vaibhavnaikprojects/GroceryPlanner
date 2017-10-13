package edu.uta.groceryplanner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
public class ListFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextView listUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null)
            startActivity(new Intent(getContext(),LoginActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list, container, false);
        listUser=(TextView) view.findViewById(R.id.listUser);
        listUser.setText(firebaseAuth.getCurrentUser().getEmail());
        return view;
    }
}
