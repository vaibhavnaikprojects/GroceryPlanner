package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.GroupBean;

public class GroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private List<FriendsBean> friendsBeans;
    private EditText mTextGroupName;
    private ImageButton addGroupBtn;
    private GroupBean groupBean;
    private String groupId;
    private DatabaseReference groupRef,groupUsersRef,friendsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        Intent intent = getIntent();
        groupBean = (GroupBean) intent.getSerializableExtra("group");
        groupRef=FirebaseDatabase.getInstance().getReference("Groups");
        if(groupBean==null) {
            setTitle("New Group");
            groupId= groupRef.push().getKey();
            groupBean=new GroupBean(groupId,"");
            groupRef.child(groupId).setValue(groupBean);
        }else {
            groupId = groupBean.getGroupId();
            setTitle(groupBean.getGroupName());
        }
        groupUsersRef=FirebaseDatabase.getInstance().getReference("GroupUsers").child(groupId);
        friendsRef=FirebaseDatabase.getInstance().getReference("friends").child(firebaseAuth.getCurrentUser().getUid());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextGroupName=findViewById(R.id.textGroupName);
        addGroupBtn=findViewById(R.id.addGroupBtn);
        recyclerView = findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        friendsBeans=new ArrayList<>();
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equalsIgnoreCase(mTextGroupName.getText().toString()))
                    mTextGroupName.setError("Enter Group Name",getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                else {
                    groupBean.setGroupName(mTextGroupName.getText().toString());
                    groupRef.child(groupId).setValue(groupBean);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ready, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu:
                openDialog();
                break;
            case R.id.menu_check:
                finish();
                break;
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.list_menu, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        TextView listMenuTitle = view.findViewById(R.id.menuTitle);
        TextView listMenuReady = view.findViewById(R.id.listMenuReady);
        TextView listMenuShare = view.findViewById(R.id.listMenuShare);
        TextView listMenuDelete = view.findViewById(R.id.listMenuDelete);
        dialog.show();
    }

    @Override
    public void onStart() {
        /*groupUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsBeans.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    friendsRef.child(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snap) {
                             FriendsBean friendsBean=snap.getValue(FriendsBean.class);
                             friendsBeans.add(friendsBean);
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
        super.onStart();*/
    }
}
