package com.example.ais_mobile_chatapp.friend;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ais_mobile_chatapp.user.UserAccount;
import com.example.chatapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddFriendListAdapter extends RecyclerView.Adapter<AddFriendListAdapter.AddFriendListHolder> {

    private ArrayList<UserAccount> userAccountsByUserName;
    private AddFriendListHolder addFriendListHolder;

    public AddFriendListAdapter(ArrayList<UserAccount> userAccountsByUserName) {

        this.userAccountsByUserName = userAccountsByUserName;

    }

    @NonNull
    @NotNull
    @Override
    public AddFriendListAdapter.AddFriendListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_add_friend_list,parent,false);
        addFriendListHolder = new AddFriendListHolder(holderView);

        return addFriendListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddFriendListAdapter.AddFriendListHolder holder, int position) {
        UserAccount userAccount = userAccountsByUserName.get(position);

        addFriendListHolder.tv_add_friend_email.setText(userAccount.getEmail());
        addFriendListHolder.tv_add_friend_name.setText(userAccount.getUser_name());

        addFriendListHolder.cb_request_add_friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // true이면 값 넣고, false이면 null
                if(isChecked) {
                    FragFriend.request_add_friend_map.put(position, userAccount);
                }else{
                    FragFriend.request_add_friend_map.put(position, null);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(userAccountsByUserName != null) {
            return userAccountsByUserName.size();
        }

        return 0;
    }

    public class AddFriendListHolder extends RecyclerView.ViewHolder {

        public TextView tv_add_friend_email, tv_add_friend_name;
        public CheckBox cb_request_add_friend;

        public AddFriendListHolder(@NonNull View itemView) {
            super(itemView);
            tv_add_friend_email = itemView.findViewById(R.id.tv_add_friend_email);
            tv_add_friend_name = itemView.findViewById(R.id.tv_add_friend_name);
            cb_request_add_friend = itemView.findViewById(R.id.cb_request_add_friend);


        }


    }
}
