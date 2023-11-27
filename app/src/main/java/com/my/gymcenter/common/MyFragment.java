package com.my.gymcenter.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.my.gymcenter.R;
import com.my.gymcenter.coach.MyPrivateCoachActivity;
import com.my.gymcenter.lesson.LessonManageActivity;
import com.my.gymcenter.utils.SharedPreferencesUtils;

import java.util.Objects;

public class MyFragment extends Fragment implements View.OnClickListener {
    private CardView MyFirst;
    private CardView MySecond;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.activity_my,container,false);
        findViewById(v);
        initView();
        return v;
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    private void initView() {
        MyFirst.setOnClickListener(this);
        MySecond.setOnClickListener(this);
    }

    private void findViewById(View v) {
        TextView welcomeMy = v.findViewById(R.id.welcome_my);
        MyFirst = v.findViewById(R.id.my_first);
        MySecond = v.findViewById(R.id.my_second);
        String WelcomeText = "欢迎您, "+ SharedPreferencesUtils.getUserInfo(getActivity()).get_username();
        String MyFirstText = getString(R.string.MyProfile);
        welcomeMy.setText(WelcomeText);
        TextView MyFirstTextView = MyFirst.findViewWithTag("text");
        MyFirstTextView.setText(MyFirstText);
        TextView MySecondTextView = MySecond.findViewWithTag("text");
        if(Objects.equals(SharedPreferencesUtils.getUserInfo(getActivity()).get_permission(), "user")){
            String MySecondText = getString(R.string.PrivateCoach);
            MySecondTextView.setText(MySecondText);
        }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(getActivity()).get_permission(), "coach")){
            String MySecondText = getString(R.string.MyLesson);
            MySecondTextView.setText(MySecondText);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_first) {
            startActivity(new Intent(getActivity(), MyProfileActivity.class));
        }else if (v.getId() == R.id.my_second) {
            if(Objects.equals(SharedPreferencesUtils.getUserInfo(getActivity()).get_permission(), "user")){
                startActivity(new Intent(getActivity(), MyPrivateCoachActivity.class));
            }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(getActivity()).get_permission(), "coach")){
                startActivity(new Intent(getActivity(), LessonManageActivity.class).putExtra("introPoint", SharedPreferencesUtils.getUserInfo(getActivity()).get_permission()));
            }
        }
    }
}
