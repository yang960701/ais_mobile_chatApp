package com.example.ais_mobile_chatapp.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ais_mobile_chatapp.user.ProfileActivity;
import com.example.ais_mobile_chatapp.user.UserAccount;

import com.example.chatapp.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.N)   // 자바8 문법 사용가능
public class FragFriend extends Fragment {

    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;
//    private FirebaseFirestore nFirestore;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private LayoutInflater nInflater;
    private View view;
    private ImageView iv_profile;
    private Button btn_add_friend_view;
    private Button btn_request_add_friend_list_view;
    private TextView tv_myName, tv_state;

    private ArrayList<UserAccount> userAccounts;
    private ArrayList<UserAccount> requestAddFriendList;
    private ArrayList<UserAccount> friendList;

    private UserAccount currentUserAccount;

    public static HashMap<Integer,UserAccount> request_add_friend_map;

    private UserAccount user;

    public static FragFriend newInstance(){
        FragFriend fragFriend = new FragFriend();
        return fragFriend;
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(
                R.layout.frag_friend, container, false);
        nInflater = inflater;
        nFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = nFirebaseAuth.getCurrentUser();
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatAPP");
//        nFirestore = FirebaseFirestore.getInstance();

        // recyclerview
        recyclerView = view.findViewById(R.id.rv_friend_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        user = new UserAccount();

        getCurrentUserAccount();    // 현재 유저 Account 업데이트
        getUserAccounts();  // 전역변수 userAccounts 업데이트됨, 가독성을 위해 return값 ArrayList<UserAccount>로 변경해도됨
        getRequestAddFriendList();  // 친구요청 List 업데이트됨
        getFriendList();    // FriendList 업데이트됨

        friendList = new ArrayList<>();
        requestAddFriendList = new ArrayList<>();

        iv_profile = view.findViewById(R.id.iv_profileSetting);
        tv_myName = view.findViewById(R.id.tv_myName);
        tv_state = view. findViewById(R.id.tv_state);


        btn_add_friend_view = view.findViewById(R.id.btn_add_friend_view);
        btn_request_add_friend_list_view = view.findViewById(R.id.btn_request_add_friend_list_view);
        // 친구요청 UserAccount map
        request_add_friend_map = new HashMap<>();

        return view;
    }

    public void getCurrentUserAccount(){
//        nFirestore.collection("relation")
//                .document(firebaseUser.getEmail())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("getCurrentUserAccount", "Error getting data", task.getException());
//                        } else {
//                            Log.e("getCurrentUserAccount", String.valueOf(task.getResult().getData()));
//                            // 자동 casting 못찾아서 수동으로 casting 했습니다
//                            HashMap<String, Object> value = (HashMap<String, Object>) task.getResult().getData();
//                            currentUserAccount = new UserAccount(value.get("idToken").toString()
//                                                                ,value.get("email").toString()
//                                                                ,value.get("user_name").toString());
//
//                            tv_myName.setText("이름 : "+currentUserAccount.getUser_name());
//                            tv_state.setText("상태 : "+String.valueOf(task.isSuccessful()));
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.e("onFailure", e.getMessage());
//                    }
//                });
    }

    public void getUserAccounts() {

        // 최초 UserAccount List get
        nDatabaseRef.child("UserAccount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("getUserAccounts", "Error getting data", task.getException());
                        } else {
                            Log.d("getUserAccounts", String.valueOf(task.getResult().getValue()));

                            // 자동 casting 못찾아서 수동으로 casting 했습니다
                            HashMap<String,Object> value = (HashMap<String, Object>) task.getResult().getValue();
                            Log.e("Error", task.getResult().getValue().toString());
                            ObjectMapper mapper = new ObjectMapper();
                            HashMap<String,UserAccount> map = mapper.convertValue(value, new TypeReference<HashMap<String,UserAccount>>() {});

                            userAccounts = new ArrayList<>();
                            map.forEach((key, userAccount) -> {userAccounts.add(userAccount);});

                        }
                    }
                });

//         데이터 변화 체크하는 onDataChange() 이벤트 추가하고 싶으나 추후
//        nDatabaseRef.child("UserAccount")

    }

//    public void requestAddFriend(UserAccount friendAccount) {
//
//        nFirestore.collection("relation")
//                .document(friendAccount.getEmail())
//                .update("request_add_friend_list", FieldValue.arrayUnion(currentUserAccount))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        if(!task.isSuccessful()){
//                            Log.e("complete", "but not successful");
//                        }else{
//                            Toast.makeText(getContext(), "친구요청 성공!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.e("failure", e.getMessage());
//                    }
//                });
//    }
//
    public void getRequestAddFriendList(){

//        nFirestore.collection("relation")
//                .document(firebaseUser.getEmail())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (task.getResult().get("request_add_friend_list") != null) {
//                                // 자동 casting 못찾아서 수동으로 casting 했습니다
//
//                                ObjectMapper mapper = new ObjectMapper();
//                                ArrayList<UserAccount> list = mapper.convertValue(task.getResult().get("request_add_friend_list")
//                                        , new TypeReference<ArrayList<UserAccount>>() {});
//                                if(list != null){
//                                    requestAddFriendList = list;
//                                    btn_request_add_friend_list_view.setText("친구요청 ("+requestAddFriendList.size()+")");
//                                } else{
//                                    btn_request_add_friend_list_view.setText("친구요청 (0)");
//                                }
//                            }
//                        }
//                    }
//                });
    }

    public void getFriendList(){
        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("FriendList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                friendList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserAccount user = dataSnapshot.getValue(UserAccount.class);
                    friendList.add(user);
                }
                adapter.notifyDataSetChanged(); // List save and refresh
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError errr) {
            }
        });
        adapter = new FriendListAdapter(friendList, getActivity(), firebaseUser, nInflater, nDatabaseRef);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        getFriendList();

        btn_add_friend_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = nInflater.inflate(R.layout.dialog_add_friend,null);
                EditText et_friend_name = view.findViewById(R.id.et_friend_name);
                Button btn_search_friend_name = view.findViewById(R.id.btn_search_friend_name);

                AlertDialog.Builder addFriendDialog = new AlertDialog.Builder(getContext());
                addFriendDialog.setTitle("친구 검색");
                addFriendDialog.setView(view)
                        .setPositiveButton("요청", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                ArrayList<UserAccount> result = new ArrayList<>();
//                                if(request_add_friend_map != null){
//                                    request_add_friend_map.forEach((key, userAccount) -> {
//                                        if(userAccount != null) {
//                                            requestAddFriend(userAccount);
//                                        }
//                                    });
//                                }
//                                request_add_friend_map.clear();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                Log.e("negative", "negative 클릭");
                                return;
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Log.e("dialog", "onDismiss");
                            }
                        })
                        .create()
                        .show();

                btn_search_friend_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String friend_name = et_friend_name.getText().toString();

                        ArrayList<UserAccount> userAccountsByUserName = new ArrayList<>();

                        userAccounts.forEach((userAccount) -> {
                            if(userAccount.getUser_name().equals(friend_name)){
                                userAccountsByUserName.add(userAccount);
                            }
                        });

                        Log.e("userAccounstByUserName", userAccountsByUserName.toString());

//                        RecyclerView rv_add_friend_list = view.findViewById(R.id.rv_add_friend_list);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                        rv_add_friend_list.setLayoutManager(layoutManager);
//                        RecyclerView.Adapter addFriendListAdapter = new AddFriendListAdapter(userAccountsByUserName);
//                        rv_add_friend_list.setAdapter(addFriendListAdapter);
                    }
                });

            }
        });

        btn_request_add_friend_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = nInflater.inflate(R.layout.dialog_request_add_friend,null);

                AlertDialog.Builder addFriendDialog = new AlertDialog.Builder(getContext());
                addFriendDialog.setTitle("친구 요청");
                addFriendDialog.setView(view)
                        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                Log.e("negative", "negative 클릭");
                                return;
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Log.e("dialog", "onDismiss");
                                getRequestAddFriendList();
                                getFriendList();
                            }
                        })
                        .create()
                        .show();

//                Log.e("btn_request_add_friend", currentUserAccount+"");
//                RecyclerView rv_request_add_friend_list = view.findViewById(R.id.rv_request_add_friend_list);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                rv_request_add_friend_list.setLayoutManager(layoutManager);
//                RecyclerView.Adapter requestAddFriendListAdapter = new RequestAddFriendListAdapter(requestAddFriendList, nFirestore, currentUserAccount);
//                rv_request_add_friend_list.setAdapter(requestAddFriendListAdapter);

            }
        });

    }


}
