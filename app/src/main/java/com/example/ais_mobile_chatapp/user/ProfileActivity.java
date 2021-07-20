package com.example.ais_mobile_chatapp.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.friend.IgnoreListAdapter;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ImageView iv_image;
    String imgName = "osz.png";    // 이미지 이름

    private Button btn_ignore_list;
    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;
//    private FirebaseFirestore nFirestore;
    private FirebaseUser firebaseUser;

    private LayoutInflater nInflater;

    private Dialog ignore_dialog;
    private Context context;

    private ArrayList<UserAccount> ignoreList;

    public static HashMap<Integer, UserAccount> ignore_friend_map;

    public static ProfileActivity newInstance(){
        ProfileActivity profileActivity = new ProfileActivity();
        return profileActivity;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nInflater = getLayoutInflater();
        ignore_dialog = new Dialog(ProfileActivity.this);
        ignore_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ignore_dialog.setContentView(R.layout.dialog_ignore_friend);


        btn_ignore_list = findViewById(R.id.btn_ignore_list);
        nFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = nFirebaseAuth.getCurrentUser();
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatAPP");
//        nFirestore = FirebaseFirestore.getInstance();


        ignoreList = new ArrayList<>();

        ignore_friend_map = new HashMap<>();


        iv_image = findViewById(R.id.iv_image);
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1); //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.

            }
        });

        // Show block list.
        btn_ignore_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ignore list", "Button Click");

                getIgnoreList();

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    iv_image.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getIgnoreList(){
        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("IgnoreList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        ignoreList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserAccount user = dataSnapshot.getValue(UserAccount.class);
                            ignoreList.add(user);
                        }

                        View view = nInflater.inflate(R.layout.dialog_ignore_friend, null);

                        RecyclerView rv_frien_list = view.findViewById(R.id.rv_ignore_friend_list);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);

                        rv_frien_list.setLayoutManager(layoutManager);
                        RecyclerView.Adapter ignoreListAdapter = new IgnoreListAdapter(ignoreList, context, firebaseUser, nInflater, nDatabaseRef);
                        rv_frien_list.setAdapter(ignoreListAdapter);
                        AlertDialog.Builder addIgnoreDialog = new AlertDialog.Builder(ProfileActivity.this);

                        addIgnoreDialog.setTitle("차단 목록");
                        addIgnoreDialog.setView(view).setPositiveButton("해제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("hashmap", ignore_friend_map.toString());
                                Log.e("Positive", "Positive click");
                                if(ignore_friend_map != null){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        ignore_friend_map.forEach((key, userAccount) -> {
                                            if(userAccount != null) {
                                                Log.e("UserAccount", userAccount.toString());
                                                requestIgnoreFriend(userAccount);
                                            }
                                        });
                                    }
                                }else{
                                    ignore_friend_map.clear();
                                }
                            }
                        }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("Negative", "negative click");
                                return;
                            }
                        }).create().show();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    public void requestIgnoreFriend(UserAccount userAccount){
        // Delete Data to Ignore Data
        // And
        // Add Data to Friend_list
        deleteIgnore(userAccount);
        addFriend(userAccount);

    }

    public void deleteIgnore(UserAccount userAccount){
        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("IgnoreList")
                .child(userAccount.getIdToken())
                .removeValue();

    }
    public void addFriend(UserAccount userAccount){
        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("FriendList")
                .child(userAccount.getIdToken())
                .setValue(userAccount);
    }


}