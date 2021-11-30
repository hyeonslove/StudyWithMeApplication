package kr.ac.koreatech.teamproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adapter.PosterQuestionListViewAdapter;
import appcomponent.MyFragment;
import entity.PosterQuestionEntity;
import kr.ac.koreatech.teamproject.databinding.FragmentPosterQuestionListBinding;

public class PosterQuestionListFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentPosterQuestionListBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PosterQuestionListViewAdapter posterQuestionListViewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String title;

    public PosterQuestionListFragment() {
        // Required empty public constructor
    }

    public PosterQuestionListFragment(String title) {
        this.title = title;
    }

    // poster_title의 모든 QnA 불러오기
    private void getPosterQnA(String poster_title) {
        db.collection("QnA" + poster_title)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String[] info = document.getId().replace("|", "@").split("@");
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                posterQuestionListViewAdapter.append(new PosterQuestionEntity(info[0],
                                        ((ArrayList<String>) document.getData().get("body")).get(0),
                                        info[1],
                                        0,
                                        transFormat.parse(info[2])));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        binding = FragmentPosterQuestionListBinding.inflate(getLayoutInflater());
        posterQuestionListViewAdapter = new PosterQuestionListViewAdapter();
        binding.createQnAButton.setOnClickListener(v -> {
            // TODO: 글쓰는 인텐트
            Intent intent = new Intent(getActivity(), QnaDialogActivity.class);
            intent.putExtra("title", title);
            startActivity(intent);

        });
        binding.framentPosterQuestionListListView.setAdapter(posterQuestionListViewAdapter);
//        posterQuestionListViewAdapter.append(new PosterQuestionEntity("질문있습니다", "아두이노 우노를 사용하고 있습니다.\n 혹시 전압강하 조절을 어떻게합니까?", "아두이노조아용", 3, new Date()));
//        posterQuestionListViewAdapter.append(new PosterQuestionEntity("액티비티란 무엇입니까?", "액티비티 생명주기등이 이해가안갑니다 ㅠㅠ\n설명해주실 고수의 답변을 기다립니다...!!", "홍길동", 1, new Date()));
//        posterQuestionListViewAdapter.append(new PosterQuestionEntity("객체지향 5대 원칙이 궁금합니다", "ISP, DIP ..등등 기억이 잘 안납니다.\n혹시 자세한 설명 또는 링크를 달아주실 분 계십니까?\n감사합니다 ㅠ", "김철수", 5, new Date()));
//        posterQuestionListViewAdapter.append(new PosterQuestionEntity("라즈베리파이 고장났는데 어케 하나요 ㅠ", "안녕하세요 컴공 김영희입니다..\n제가 라즈비안을 깔고 부팅하는데 부팅이 안됩니다.\n혹시 어느 부분이 문제인지 알려주실 수 있으십니까...? 부탁드립니다 ㅠ\n이문제로 진짜 몇 밤을 넘게 못잤습니다 ㅠㅠ\n라즈베리 파이 전원을 연결하고 + - 체크하고 모터 굴리는데 납질이 잘못되어서 합선이 난건지 라즈베리파이가 푹 하고 꺼지더니 반응이 없습니다.. sd카드가 손상 된 것일까요?\n 하 정말 슬픕니다 살려주세요.", "김영희", 7, new Date()));
        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        binding.framentPosterQuestionListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final PosterQuestionEntity item = (PosterQuestionEntity) posterQuestionListViewAdapter.getItem(a_position);
                MyFragment.changeFragment(new PosterQuestionMainFragment(item, title));
                //텍스트뷰에 출력
                System.out.println(item.getTitle() + " 에 질문글에 접속함?");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        posterQuestionListViewAdapter.clear();
        getPosterQnA(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("질의응답 목록");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {

            case android.R.id.home:
                MyFragment.prevFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}