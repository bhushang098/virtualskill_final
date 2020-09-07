/*
 * Copyright (C) 2019 Twilio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twilio.video.app.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.twilio.video.app.R;
import com.twilio.video.app.auth.Authenticator;
import com.twilio.video.app.auth.CommunityLoginResult.CommunityLoginFailureResult;
import com.twilio.video.app.auth.CommunityLoginResult.CommunityLoginSuccessResult;
import com.twilio.video.app.auth.LoginEvent.CommunityLoginEvent;
import com.twilio.video.app.auth.LoginResult;
import com.twilio.video.app.base.BaseActivity;
import com.twilio.video.app.data.api.AuthServiceError;
import com.twilio.video.app.ui.room.RoomActivity;
import com.twilio.video.app.util.InputUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

// TODO Create view model and fragment for this screen
public class CommunityLoginActivity extends BaseActivity {

    @Inject
    Authenticator authenticator;

    @BindView(R.id.community_login_screen_progressbar)
    ProgressBar progressBar;

    CompositeDisposable disposable = new CompositeDisposable();
    String roomName, userName;

    @Override
    protected void onStart() {

        login(getIntent().getStringExtra("user_name"), "1812261516");
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomName = getIntent().getStringExtra("room_name");
        userName = getIntent().getStringExtra("user_name");


        setContentView(R.layout.development_activity_login);

        ButterKnife.bind(this);
        if (authenticator.loggedIn()) startLobbyActivity(roomName,userName);
    }


    private void login(String identity, String passcode) {
         preLoginViewState();
        disposable.add(
                authenticator
                        .login(new CommunityLoginEvent(identity, passcode))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(this::postLoginViewState)
                        .subscribe(
                                loginResult -> {
                                    if (loginResult instanceof CommunityLoginSuccessResult)
                                        startLobbyActivity(roomName,userName);
                                    else {
                                        handleAuthError(loginResult);
                                    }
                                },
                                exception -> {
                                    handleAuthError(null);
                                    Timber.e(exception);
                                }));
    }

    private void handleAuthError(LoginResult loginResult) {

        if (loginResult instanceof CommunityLoginFailureResult) {
            String errorMessage;
            AuthServiceError error = ((CommunityLoginFailureResult) loginResult).getError();
            switch (error) {
                case INVALID_PASSCODE_ERROR:
                    errorMessage = getString(R.string.login_screen_invalid_passcode_error);
                    //passcodeTextInputLayout.setError(errorMessage);
                    //passcodeTextInputLayout.setErrorEnabled(true);
                    return;
                case EXPIRED_PASSCODE_ERROR:
                    errorMessage = getString(R.string.login_screen_expired_passcode_error);
                  //  passcodeTextInputLayout.setError(errorMessage);
                    //passcodeTextInputLayout.setErrorEnabled(true);
                    return;
            }
        }

        displayAuthError();
    }

    private void preLoginViewState() {
        InputUtils.hideKeyboard(this);
       // enableLoginButton(false);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setBackgroundColor(Color.WHITE);

       // passcodeTextInputLayout.setErrorEnabled(false);
    }

    private void postLoginViewState() {
        progressBar.setVisibility(View.GONE);
        //enableLoginButton(true);
    }


    private void startLobbyActivity(String roomName,String userName) {
        Intent i = new Intent(CommunityLoginActivity.this, RoomActivity.class);
        i.putExtra("room_name",roomName);
        i.putExtra("user_name",userName);
       //RoomActivity.startActivity(this,getIntent().getData());
       startActivity(i);
        finish();
    }

    private void displayAuthError() {
        new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
                .setTitle(getString(R.string.login_screen_error_title))
                .setMessage(getString(R.string.login_screen_auth_error_desc))
                .setPositiveButton("OK", null)
                .show();
    }
}
