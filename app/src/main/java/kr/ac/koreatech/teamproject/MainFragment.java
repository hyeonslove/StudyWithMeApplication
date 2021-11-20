package kr.ac.koreatech.teamproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import adapter.FrontRecyclerViewAdapter;
import entity.FrontPoster;
import kr.ac.koreatech.teamproject.databinding.FragmentMainBinding;
import service.TimerService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private Date startTime = new Date();

    private BroadcastReceiver studyTimeRecvier;

    private FrontRecyclerViewAdapter m1Adapter;
    private FrontRecyclerViewAdapter m2Adapter;
    private LinearLayoutManager m1LayoutManager;
    private LinearLayoutManager m2LayoutManager;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (studyTimeRecvier == null) {
            studyTimeRecvier = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Integer total_sec = intent.getIntExtra("time_sec", 0);
                    if (total_sec == null)
                        return;
                    int sec = (total_sec / 1000) % 60;
                    int min = (total_sec / (1000 * 60)) % 60;
                    int hour = (total_sec / (1000 * 60 * 60)) % 24;
                    binding.timerTextView.setText(hour + ":" + min + ":" + sec);
                }
            };
        }

        requireActivity().registerReceiver(studyTimeRecvier, new IntentFilter("timeService"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (studyTimeRecvier != null) {
            requireActivity().unregisterReceiver(studyTimeRecvier);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        binding = FragmentMainBinding.inflate(getLayoutInflater());

        // init Data
        ArrayList<FrontPoster> data1 = new ArrayList<>();
        binding.btnPlay.setOnClickListener(v -> {
            requireActivity().startService(new Intent(getActivity(), TimerService.class));
        });

        binding.btnStop.setOnClickListener(v -> {
            requireActivity().stopService(new Intent(getActivity(), TimerService.class));
            System.out.println("stop 버튼 누름?");
        });
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "모바일프로그래밍"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "중국어회화"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "창의적글쓰기"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "IoT개론및실습"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "스크립트프로그래밍"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "객체지향개발론및실습"));
//        data1.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "컴퓨터네트워크"));

/*
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if
          request.time < timestamp.date(2022, 12, 18);
    }
  }
}
 */
        ArrayList<FrontPoster> data2 = new ArrayList<>();

//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "정처기합격"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "CAD 스터디"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "C언어 모각코"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "과탑먹을사람들"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "모각코"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "공대생들어와"));
//        data2.add(new FrontPoster(BitmapFactory.decodeResource(getResources(), R.drawable.default_image), "중국어같이해"));
        // init LayoutManager
        m1LayoutManager = new LinearLayoutManager(this.getActivity());
        m1LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        m2LayoutManager = new LinearLayoutManager(this.getActivity());
        m2LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        // setLayoutManager
        binding.fragmentMainPosterListView.setLayoutManager(m1LayoutManager);
        binding.fragmentMainStudyListView.setLayoutManager(m2LayoutManager);


        // init Adapter
        m1Adapter = new FrontRecyclerViewAdapter();
        m2Adapter = new FrontRecyclerViewAdapter();
        // set Data
        m1Adapter.setData(data1);
        m2Adapter.setData(data2);

        // set Adapter
        binding.fragmentMainPosterListView.setAdapter(m1Adapter);

        binding.fragmentMainStudyListView.setAdapter(m2Adapter);
    }


    public void finishStudy(int hour, int min, int sec) {
        requireActivity().runOnUiThread(() -> {
            // TODO: 계산은 본인 몫
            binding.timerTextView.setText(hour + ":" + min + ":" + sec);
        });
        System.out.println("공부시간이 측정되었습니다");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Study With Me"); //  ·८·
        actionBar.setDisplayHomeAsUpEnabled(false);
        return binding.getRoot();
    }

}