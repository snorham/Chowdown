package com.example.chowdown.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.chowdown.R;

public class LoginDialogFragment extends DialogFragment {


    public LoginDialogFragment() {
    }

    EditText mUsernameEditText;
    Button mSaveButton;
    public static final String USERNAME_KEY = "USERNAME_KEY";
    public static final String LOG_TAG = "LoginDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_fragment_login, container, false);

        mUsernameEditText = (EditText) root.findViewById(R.id.editText_username_entry);
        mSaveButton = (Button) root.findViewById(R.id.button_save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUsernameEditText.getText().toString().trim().length() != 0){

                    String username = (mUsernameEditText.getText().toString());
                    Log.d(LOG_TAG, "THE USERNAME IS: " + username);

                    //saves the "saved" username in the editText to SharedPreferences
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit()
                            .putString(USERNAME_KEY, username)
                            .commit();

                    //jumps back to main activity???
                    dismiss();
                }

            }

        });

        // Inflate the layout for this fragment
        return root;
    }
}