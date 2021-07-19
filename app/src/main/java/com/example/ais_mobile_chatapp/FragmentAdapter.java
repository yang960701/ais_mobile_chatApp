package com.example.ais_mobile_chatapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ais_mobile_chatapp.friend.FragFriend;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter extends FragmentStateAdapter {


    public FragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragFriend();
            case 1:
                return new FragChatList();
            case 2:
                return new FragSetting();
            case 3:
                return new FragAnother();

        }

        return new FragFriend();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}



