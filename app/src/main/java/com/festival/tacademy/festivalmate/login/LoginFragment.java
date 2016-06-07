package com.festival.tacademy.festivalmate.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.festival.tacademy.festivalmate.Data.MySignInResult;
import com.festival.tacademy.festivalmate.Data.User;
import com.festival.tacademy.festivalmate.HomeActivity;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.R;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment {

    EditText emailView; // 이메일 입력
    EditText passwordView; // 패스워드 입력
    Button facebookLoginButton;


    public LoginFragment() {
        // Required empty public constructor
    }


//    private CallbackManager callbackManager = null;
//    private AccessTokenTracker accessTokenTracker = null;
//    private com.facebook.login.widget.LoginButton loginButton = null;
//
//    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            AccessToken accessToken = loginResult.getAccessToken();
//            Profile profile = Profile.getCurrentProfile();
//            Toast.makeText(getActivity().getApplicationContext(), loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
//// Toast.makeText(getActivity().getApplicationContext(), loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCancel() {
//            Toast.makeText(getActivity().getApplicationContext(), "User sign in canceled!", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//
//        }
//    };
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//// App code
//                Log.i("eNuri", "Current Token : " + currentAccessToken);
//            }
//        };
//        accessTokenTracker.startTracking();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        accessTokenTracker.stopTracking();
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        LoginButton loginButton = (LoginButton)view.findViewById(R.id.btn_facebook);
//        loginButton.setReadPermissions("public_profile", "user_friends");
//        loginButton.setFragment(this);
//        loginButton.registerCallback(callbackManager, callback);
//    }
//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        emailView = (EditText)view.findViewById(R.id.edit_name);
        passwordView = (EditText)view.findViewById(R.id.edit_password);

        facebookLoginButton = (Button)view.findViewById(R.id.btn_facebook_login);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook_login();
            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() { //로그인 하기 버튼 클릭시

            @Override
            public void onClick(View v) {
                final String email = emailView.getText().toString();
                final String password = passwordView.getText().toString();

                NetworkManager.getInstance().signin(getContext(), email, password, new NetworkManager.OnResultListener<MySignInResult>() {
                    @Override
                    public void onSuccess(Request request, MySignInResult result) { // 로그인 성공하면

                        if (result.success == 1) {
                            Toast.makeText(getContext(),"로그인 성공 : "+ result.message,Toast.LENGTH_SHORT ).show();
                            PropertyManager.getInstance().setLogin(true);
                            PropertyManager.getInstance().setUser(result.result);
                            PropertyManager.getInstance().setEmail(email);
                            PropertyManager.getInstance().setNo(result.result.mem_no);
                            PropertyManager.getInstance().setPassword(password);

                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent); // 일단 홈으로 이동
                            getActivity().finish();
                        }
                    }
                    @Override
                    public void onFail(Request request, IOException exception) { // 로그인 실패하면

                        Toast.makeText(getContext(),"로그인 실패 : "+ exception,Toast.LENGTH_SHORT ).show();
                    }
                });
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

    CallbackManager callbackManager;
    LoginManager loginManager;
    AccessTokenTracker tokenTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tokenTracker == null) {
            tokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    updateButtonText();
                }
            };
        } else {
            tokenTracker.startTracking();
        }
        updateButtonText();
    }

    @Override
    public void onStop() {
        super.onStop();
        tokenTracker.stopTracking();
    }
    private void updateButtonText() {
        if (isLogin()) {
            facebookLoginButton.setText("facebook logout");
        } else {
            facebookLoginButton.setText("facebook login");
        }
    }
    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token!=null;
    }


    private void facebook_login() {

        if (!isLogin()) {
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    final AccessToken token = AccessToken.getCurrentAccessToken();
                    NetworkManager.getInstance().login_fb(getContext(), token.getToken(), PropertyManager.getInstance().getRegistrationToken(), new NetworkManager.OnResultListener<MySignInResult>() {
                        @Override
                        public void onSuccess(Request request, MySignInResult result) {

                            Toast.makeText(getContext(), "페이스북 로그인"+result.result.getName()+" "+result.result.getMem_id(),Toast.LENGTH_SHORT).show();
                            User user = (User)result.result;
                            // login success
                            PropertyManager.getInstance().setLogin(true);
                            PropertyManager.getInstance().setNo(user.getMem_no());
                            PropertyManager.getInstance().setUser(user);
                            PropertyManager.getInstance().setFacebookId(user.mem_id);
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent); // 일단 홈으로 이동

                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                            Toast.makeText(getContext(), "실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            loginManager.logInWithReadPermissions(this, Arrays.asList("email"));
        } else {
            loginManager.logOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
