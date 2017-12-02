package edu.uta.groceryplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.FriendsAdapter;
import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.GroupBean;

public class GroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private List<FriendsBean> friendsBeans, groupFriends;
    private EditText mTextGroupName;
    private ImageButton addUsers;
    private GroupBean groupBean;
    private String groupId;
    private int groupCount;
    private DatabaseReference groupRef, groupUsersRef, friendsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        Intent intent = getIntent();
        groupBean = (GroupBean) intent.getSerializableExtra("groupBean");
        groupRef = FirebaseDatabase.getInstance().getReference("Groups");
        mTextGroupName = findViewById(R.id.textGroupName);
        if (groupBean == null) {
            setTitle("New Group");
            groupId = groupRef.push().getKey();
            groupBean = new GroupBean(groupId, "", 0);
            groupRef.child(groupId).setValue(groupBean);
        } else {
            groupId = groupBean.getGroupId();
            setTitle(groupBean.getGroupName());
            mTextGroupName.setText(groupBean.getGroupName());
        }
        groupFriends = new ArrayList<>();
        groupUsersRef = FirebaseDatabase.getInstance().getReference("GroupUsers").child(groupId);
        friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(firebaseAuth.getCurrentUser().getUid());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addUsers = findViewById(R.id.addUsers);
        recyclerView = findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        friendsBeans = new ArrayList<>();
        adapter = new FriendsAdapter(groupFriends, getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View modal = getLayoutInflater().inflate(R.layout.predefined_products, null);
                final ListView listView = modal.findViewById(R.id.lv);
                final ArrayAdapter<FriendsBean> arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, friendsBeans);
                listView.setAdapter(arrayAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                AlertDialog dialog = new AlertDialog.Builder(GroupActivity.this)
                        .setTitle(groupBean.getGroupName().equalsIgnoreCase("") ? "New Group" : groupBean.getGroupName())
                        .setView(modal)
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int arg) {
                                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                                if (checkedItems != null) {
                                    for (int i = 0; i < checkedItems.size(); i++) {
                                        if (checkedItems.valueAt(i)) {
                                            FriendsBean friendsBean = (FriendsBean) listView.getAdapter().getItem(checkedItems.keyAt(i));
                                            groupUsersRef.child(friendsBean.getFriendId()).setValue(friendsBean.getFriendId());
                                        }
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).create();
                dialog.show();
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
                if (mTextGroupName.getText().toString().equalsIgnoreCase(""))
                    mTextGroupName.setError("Enter Group Name");
                else {
                    groupBean.setPeopleCount(groupFriends.size());
                    groupBean.setGroupName(mTextGroupName.getText().toString());
                    groupRef.child(groupId).setValue(groupBean);
                    finish();
                }
                break;
            default:
                if (groupCount <= 1) {
                    groupRef.child(groupId).removeValue();
                    groupUsersRef.removeValue();
                }
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.group_menu, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        TextView listMenuTitle = view.findViewById(R.id.menuTitle);
        TextView listMenuDelete = view.findViewById(R.id.listMenuDelete);
        listMenuTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listMenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupRef.child(groupId).removeValue();
                groupUsersRef.removeValue();
                dialog.dismiss();
                finish();
            }

        });
        dialog.show();
    }

    @Override
    public void onStart() {
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FriendsBean friendsBean = snapshot.getValue(FriendsBean.class);
                    friendsBeans.add(friendsBean);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        groupUsersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friendsRef.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        FriendsBean friendsBean = snap.getValue(FriendsBean.class);
                        groupFriends.add(friendsBean);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

}
