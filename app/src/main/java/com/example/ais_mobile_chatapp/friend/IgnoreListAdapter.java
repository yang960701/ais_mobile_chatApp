package com.example.ais_mobile_chatapp.friend;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.user.ProfileActivity;
import com.example.ais_mobile_chatapp.user.UserAccount;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IgnoreListAdapter extends RecyclerView.Adapter<IgnoreListAdapter.CustomViewHolder> {

    private ArrayList<UserAccount> ignoreList;
    private FirebaseUser firebaseUser;
    private LayoutInflater nInflater;

    private CustomViewHolder viewHolder;


    private FirebaseFirestore nFirestore;
    private ProfileActivity profileActivity;


    public IgnoreListAdapter(ArrayList<UserAccount> ignoreList , LayoutInflater nInflater, FirebaseUser firebaseUser) {
        this.ignoreList = ignoreList;
        this.firebaseUser = firebaseUser;
        this.nInflater = nInflater;

    }

    @NonNull
    @NotNull
    @Override
    public IgnoreListAdapter.CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ignore_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull IgnoreListAdapter.CustomViewHolder holder, int position) {
        UserAccount userAccount = ignoreList.get(position);

        holder.tv_ignore_friend_email.setText(ignoreList.get(position).getEmail());
        holder.tv_ignore_friend_name.setText(ignoreList.get(position).getUser_name());


        holder.cb_ignore_friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // True
                    Log.e("Ignore CheckBox", "True");
                    Log.e("Inore Email", userAccount.getEmail().toString());
                    ProfileActivity.ignore_friend_map.put(position, userAccount);
                } else{
                    // False
                    Log.e("Ignore CheckBox", "False");
                    Log.e("Inore Email", holder.tv_ignore_friend_email.getText().toString());
                    ProfileActivity.ignore_friend_map.put(position, null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != ignoreList ? ignoreList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ignore_friend_name;
        TextView tv_ignore_friend_email;
        CheckBox cb_ignore_friend;


        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.tv_ignore_friend_email = (TextView) itemView.findViewById(R.id.tv_ignore_friend_email);
            this.tv_ignore_friend_name = (TextView) itemView.findViewById(R.id.tv_ignore_friend_name);
            this.cb_ignore_friend = (CheckBox) itemView.findViewById(R.id.cb_ignore_friend);
        }
    }
}
