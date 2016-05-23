package com.festival.tacademy.festivalmate.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.festival.tacademy.festivalmate.HomeActivity;
import com.festival.tacademy.festivalmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText emailView; // 이메일 입력
    EditText passwordView; // 패스워드 입력
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        emailView = (EditText)view.findViewById(R.id.edit_email);
        passwordView = (EditText)view.findViewById(R.id.edit_password);

        Button btn = (Button)view.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() { //로그인 하기 버튼 클릭시
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "로그인",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent); // 일단 홈으로 이동
                getActivity().finish();
            }
        });

        btn = (Button)view.findViewById(R.id.btn_facebook_login);
        btn.setOnClickListener(new View.OnClickListener() { // 페이스북 로그인 버튼 클릭시
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "페이스북 로그인",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), HomeActivity.class)); // 일단 홈으로 이동
                getActivity().finish();
            }
        });

        btn = (Button)view.findViewById(R.id.btn_kaokaotalk_login);
        btn.setOnClickListener(new View.OnClickListener() { // 카카오톡 로그인 버튼 클릭시
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "카카오톡 로그인",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), HomeActivity.class)); // 일단 홈으로 이동
                getActivity().finish();
            }
        });

        btn= (Button)view.findViewById(R.id.btn_email_signup);
        btn.setOnClickListener(new View.OnClickListener() { // 이메일가입하기 버튼 클릭시
            @Override
            public void onClick(View v) {

                ((LoginActivity)getActivity()).changeSignUp(); // 회원가입으로 이동


            }
        });

        return view;

    }

}
