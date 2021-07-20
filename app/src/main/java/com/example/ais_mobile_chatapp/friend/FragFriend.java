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
//import com.google.firebase.database.FieldValue;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)   // 자바8 문법 사용가능
public class FragFriend extends Fragment {

    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;
    private FirebaseUser firebaseUser;

    private RecyclerView friendListRV, addFriendListRV, requestFriendListRV;
    private RecyclerView.Adapter friendListAdapter, addFriendListAdapter, requestFriendListAdapter;

    private LayoutInflater nInflater;
    private View view;

    private ImageView iv_profile_setting_view,iv_my_image;
    private TextView tv_my_user_name, tv_my_intro;

    private ImageView iv_add_friend_view,iv_request_friend_view;
    private TextView tv_request_friend_count;

    private ArrayList<UserAccount> userAccountsByUserName;
    private ArrayList<UserAccount> requestAddFriendList;
    private ArrayList<UserAccount> friendList;

    private UserAccount currentUserAccount;

    public static HashMap<String,UserAccount> request_add_friend_map;

    private UserAccount user;

    private ObjectMapper objectMapper;

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

        // recyclerview
        friendListRV = view.findViewById(R.id.rv_friend_list);
        friendListRV.setHasFixedSize(true);
        friendListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        user = new UserAccount();
        friendList = new ArrayList<>();
        requestAddFriendList = new ArrayList<>();

        iv_profile_setting_view = view.findViewById(R.id.iv_profile_setting_view);
        iv_my_image = view.findViewById(R.id.iv_my_image);
        tv_my_user_name = view.findViewById(R.id.tv_my_user_name);
        tv_my_intro = view. findViewById(R.id.tv_my_intro);

        iv_add_friend_view = view.findViewById(R.id.iv_add_friend_view);
        iv_request_friend_view = view.findViewById(R.id.iv_request_friend_view);
        tv_request_friend_count = view.findViewById(R.id.tv_request_friend_count);

        // 친구요청 UserAccount map
        request_add_friend_map = new HashMap<>();
        objectMapper = new ObjectMapper();

        getCurrentUserAccount();    // 현재 유저 Account 업데이트
//        getUserAccounts();  // 전역변수 userAccounts 업데이트됨, 가독성을 위해 return값 ArrayList<UserAccount>로 변경해도됨
        getRequestAddFriendList();  // 친구요청 List 업데이트됨
        getFriendList();    // FriendList 업데이트됨

        return view;
    }

    public void getCurrentUserAccount(){
        nDatabaseRef.child("UserAccount")
                .child(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("getCurrentUserAccount", "Error getting data", task.getException());
                        } else {
                            // 자동 casting 못찾아서 수동으로 casting 했습니다
                            Log.e("task.getResult()", task.getResult().getValue()+"");
                            currentUserAccount = task.getResult().getValue(UserAccount.class);
                            tv_my_user_name.setText("이름 : "+currentUserAccount.getUser_name());

                            // UserAccount에 intro 추가해야함
//                            tv_my_intro.setText("상태 : "+String.valueOf(task.isSuccessful()));
                        }

                    }
                });
    }

//    public void getUserAccounts() {
//
//        // 최초 UserAccount List get
//        nDatabaseRef.child("UserAccount")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("getUserAccounts", "Error getting data", task.getException());
//                        } else {
//                            Log.d("getUserAccounts", String.valueOf(task.getResult().getValue()));
//
//                            userAccounts.clear();
//                            for(DataSnapshot child : task.getResult().getChildren()){
//                                user = child.getValue(new GenericTypeIndicator<UserAccount>() {});
//                                userAccounts.add(user);
//                            }
//                        }
//                    }
//                });
//
//        // 데이터 변화 체크하는 onDataChange() 이벤트 추가하고 싶으나 추후
//
//    }

    public void requestAddFriend() {

        Map<String, Object> updates = new HashMap<>();
        request_add_friend_map.forEach((key, value) -> {
            if(value != null){
                updates.put(key+"/RequestAddFriendList/"+currentUserAccount.getIdToken(), currentUserAccount);
            }
        });

        nDatabaseRef.child("Relation")
                .updateChildren(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Log.e("complete", "but not successful");
                        }else{
                            Toast.makeText(getContext(), "친구요청 성공!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e("failure", e.getMessage());
                        Toast.makeText(getContext(), "친구요청 실패, 다시 한번 확인하시오", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getRequestAddFriendList(){

        nDatabaseRef.child("Relation")
                .child(firebaseUser.getUid())
                .child("RequestAddFriendList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("getRequestAddFriendList", task.getException().toString());
                        }else{
                            // 자동 casting 못찾아서 수동으로 casting 했습니다
                            if(task.getResult() != null){
                                requestAddFriendList.clear();
                                for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                                    user = dataSnapshot.getValue(UserAccount.class);
                                    requestAddFriendList.add(user);
                                }
                                tv_request_friend_count.setText("("+requestAddFriendList.size()+")");
                            }
                        }
                    }
                });
    }

    public void getFriendList(){
        nDatabaseRef.child("Relation")
                .child(firebaseUser.getUid())
                .child("FriendList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        friendList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserAccount user = dataSnapshot.getValue(UserAccount.class);
                            friendList.add(user);
                        }
                        friendListAdapter.notifyDataSetChanged(); // List save and refresh

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError errr) {
                    }
        });

        friendListAdapter = new FriendListAdapter(friendList);
        friendListRV.setAdapter(friendListAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        iv_profile_setting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        getFriendList();


        iv_add_friend_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = nInflater.inflate(R.layout.dialog_add_friend,null);
                EditText et_friend_name = view.findViewById(R.id.et_friend_name);
                Button btn_search_friend_name = view.findViewById(R.id.btn_search_friend_name);

                addFriendListRV = view.findViewById(R.id.rv_add_friend_list);
                addFriendListRV.setHasFixedSize(true);
                addFriendListRV.setLayoutManager(new LinearLayoutManager(getContext()));

                StringBuffer friend_name = new StringBuffer();


                AlertDialog.Builder addFriendDialog = new AlertDialog.Builder(getContext());
                addFriendDialog.setTitle("친구 검색");
                addFriendDialog.setView(view)
                        .setPositiveButton("요청", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(friend_name.toString() != null) {
                                    requestAddFriend();
                                }

                                request_add_friend_map.clear();
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
                        friend_name.setLength(0);
                        request_add_friend_map.clear();
                        friend_name.append(et_friend_name.getText().toString());

                        userAccountsByUserName = new ArrayList<>();

                        nDatabaseRef.child("UserAccount")
                                .orderByChild("user_name")
                                .equalTo(friend_name.toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            user = dataSnapshot.getValue(UserAccount.class);
                                            userAccountsByUserName.add(user);
                                        }
                                        addFriendListAdapter.notifyDataSetChanged();

                                        Log.e("byUserName", userAccountsByUserName.toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                        addFriendListAdapter = new AddFriendListAdapter(userAccountsByUserName);
                        addFriendListRV.setAdapter(addFriendListAdapter);
                    }
                });

            }
        });

        iv_request_friend_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = nInflater.inflate(R.layout.dialog_request_add_friend,null);

                requestFriendListRV = view.findViewById(R.id.rv_request_add_friend_list);
                requestFriendListRV.setHasFixedSize(true);
                requestFriendListRV.setLayoutManager(new LinearLayoutManager(getContext()));
                requestFriendListAdapter = new RequestAddFriendListAdapter(requestAddFriendList, currentUserAccount);
                requestFriendListRV.setAdapter(requestFriendListAdapter);

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

            }
        });

    }

}
