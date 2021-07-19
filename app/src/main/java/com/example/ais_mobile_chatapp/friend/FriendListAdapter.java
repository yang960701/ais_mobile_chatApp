package com.example.ais_mobile_chatapp.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.user.UserAccount;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListHolder> {

    private ArrayList<UserAccount> friendList;
    private FriendListHolder friendListHolder;
    private LayoutInflater nInflater;
    private FirebaseUser firebaseUser;
    private FragFriend fragFriend;


    private FirebaseFirestore nFirestore;


    public FriendListAdapter(ArrayList<UserAccount> friendList ,LayoutInflater nInflater, FirebaseUser firebaseUser) {
        this.friendList = friendList;
        this.nInflater = nInflater;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @NotNull
    @Override
    public FriendListAdapter.FriendListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friend_list,parent,false);
        friendListHolder = new FriendListHolder(holderView);

        return friendListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendListAdapter.FriendListHolder holder, int position) {
        UserAccount userAccount = friendList.get(position);

        friendListHolder.tv_friend_email.setText(userAccount.getEmail());
        friendListHolder.tv_friend_name.setText(userAccount.getUser_name());
        nFirestore = FirebaseFirestore.getInstance();

        fragFriend = new FragFriend();

        friendListHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = nInflater.inflate(R.layout.dialog_friend_profile,null);
                TextView tv_friend_email = view.findViewById(R.id.tv_friend_email);
                TextView tv_friend_name = view.findViewById(R.id.tv_friend_name);
                Button btn_delete_friend = view.findViewById(R.id.btn_delete_friend);
                Button btn_ignore_friend = view.findViewById(R.id.btn_ignore_friend);

                tv_friend_email.setText(userAccount.getEmail());
                tv_friend_name.setText(userAccount.getUser_name());


                AlertDialog.Builder friendProfileDialog = new AlertDialog.Builder(friendListHolder.itemView.getContext());
                friendProfileDialog.setTitle("친구 프로필");
                friendProfileDialog.setView(view)
//                        .setNeutralButton("닫기", new DialogInterface.OnClickListener() {   // 왼쪽으로 나와서 negative
                        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                                Log.e("onDisMiss", "dismiss");
                            }
                        })
                        .create()
                        .show();

                // Writer : Park
                // Delete Friend Data
                btn_delete_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriend(userAccount);

                    }
                });

                // ignore freind data
                btn_ignore_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ignoreFriend(userAccount);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(friendList != null) {
            return friendList.size();
        }
        return 0;
    }

    public class FriendListHolder extends RecyclerView.ViewHolder {

        public ImageView iv_friend_image;
        public TextView tv_friend_email, tv_friend_name;


        public FriendListHolder(@NonNull View itemView) {
            super(itemView);

            iv_friend_image = itemView.findViewById(R.id.iv_friend_image);
            tv_friend_email = itemView.findViewById(R.id.tv_friend_email);
            tv_friend_name = itemView.findViewById(R.id.tv_friend_name);

        }


    }

    public void deleteFriend(UserAccount userAccount){
        Map<String, Object> updates = new HashMap<>();
        updates.put("friend_list", FieldValue.arrayRemove(userAccount));
        nFirestore.collection("relation").document(firebaseUser.getEmail().toString())
                .update(updates);

    }
    public void ignoreFriend(UserAccount userAccount){
        deleteFriend(userAccount);
        Map<String, Object> updates = new HashMap<>();
        updates.put("ignore_list", FieldValue.arrayUnion(userAccount));
        nFirestore.collection("relation").document(firebaseUser.getEmail().toString())
                .update(updates);
    }
}
