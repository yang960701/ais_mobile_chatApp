package com.example.ais_mobile_chatapp.friend;

import android.app.AlertDialog;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.user.UserAccount;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private ArrayList<UserAccount> friendList;
    private int row_index = -1;

    public FriendListAdapter(ArrayList<UserAccount> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public FriendListAdapter.FriendListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friend_list, parent, false);
        FriendListViewHolder holder = new FriendListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendListAdapter.FriendListViewHolder holder, int position) {

        UserAccount currentItem = friendList.get(position);

        // image,
//        if(currentItem.getImage() != null){
//            try{
//                Picasso.get().load(currentItem.getImage()).into(holder.iv_friend_image);
//            }catch (Exception e){
//                holder.iv_friend_image.setImageResource(R.mipmap.ic_launcher);
//            }
//        }else {
//            holder.iv_friend_image.setImageResource(R.mipmap.ic_launcher);
//        }
        holder.iv_friend_image.setImageResource(R.mipmap.ic_launcher);  // 임시

//        holder.tv_friend_intro.setText(currentItem.getIntro() != null ? currentItem.getIntro() : "");
        holder.tv_friend_user_name.setText(currentItem.getUser_name());

        holder.view_friend_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if(row_index == position){
            holder.view_friend_list_item.setBackgroundResource(R.drawable.friendlist_item_selected_bg);
        } else {
            holder.view_friend_list_item.setBackgroundResource(R.drawable.friendlist_item_bg);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v.inflate(v.getContext(),R.layout.dialog_friend_profile, null);

                TextView tv_friend_email = view.findViewById(R.id.tv_friend_email);
                TextView tv_friend_name = view.findViewById(R.id.tv_friend_name);

                Button btn_add_singlechat = view.findViewById(R.id.btn_add_singlechat);
                Button btn_delete_friend = view.findViewById(R.id.btn_delete_friend);
                Button btn_ignore_friend = view.findViewById(R.id.btn_ignore_friend);

                tv_friend_email.setText(currentItem.getEmail());
                tv_friend_name.setText(currentItem.getUser_name());

                AlertDialog.Builder friendProfileDialog = new AlertDialog.Builder(holder.itemView.getContext());
                friendProfileDialog.setTitle("친구 프로필");
                friendProfileDialog.setView(view)
                        .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.e("onDisMiss", "dismiss");
                    }
                }).create().show();

                btn_delete_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriend(currentItem);

                    }
                });

                btn_ignore_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ignoreFriend(currentItem);
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

    public class FriendListViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_friend_image;
        TextView tv_friend_user_name, tv_friend_intro;
        ConstraintLayout view_friend_list_item;

        public FriendListViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            this.iv_friend_image = itemView.findViewById(R.id.iv_friend_image);
            this.tv_friend_user_name = itemView.findViewById(R.id.tv_friend_user_name);
            this.tv_friend_intro = itemView.findViewById(R.id.tv_friend_intro);
            this.view_friend_list_item = itemView.findViewById(R.id.view_friend_list_item);
        }


    }

    public void deleteFriend(UserAccount userAccount){
        Log.e("delete", userAccount.toString());
//        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("FriendList")
//                .child(userAccount.getIdToken())
//                .removeValue();

    }
    public void ignoreFriend(UserAccount userAccount){
        Log.e("ignore", userAccount.toString());
//        deleteFriend(userAccount);
//        nDatabaseRef.child("Relation").child(firebaseUser.getUid()).child("IgnoreList")
//                .child(userAccount.getIdToken())
//                .setValue(userAccount);
    }
}
