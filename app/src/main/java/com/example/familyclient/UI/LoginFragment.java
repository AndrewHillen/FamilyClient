package com.example.familyclient.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familyclient.Net.DataTask;
import com.example.familyclient.Net.Listener;
import com.example.familyclient.Net.LoginTask;
import com.example.familyclient.Net.RegisterTask;
import com.example.familyclient.Net.ServerProxy;
import com.example.familyclient.R;

import ReqRes.LoginRequest;
import ReqRes.RegisterRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LoginFragment extends Fragment implements Listener
{


    // TODO: Rename and change types of parameters
    private static final String ARG_LISTENER = "listener";
    EditText hostText;
    EditText portText;
    EditText usernameText;
    EditText passwordText;
    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    RadioButton maleBox;
    RadioButton femaleBox;
    Button loginButton;
    Button registerButton;
    Listener listener;

    TextWatcher loginWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            if(loginChecker())
            {
                loginButton.setEnabled(true);
            }
            else
            {
                loginButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    TextWatcher registerWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            if(registerChecker() && buttonChecker())
            {
                registerButton.setEnabled(true);
            }
            else
            {
                registerButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    private boolean buttonChecker()
    {
        if(!maleBox.isChecked() && !femaleBox.isChecked())
        {
            return false;
        }
        return true;
    }





    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public LoginFragment(Listener l) {
        listener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        hostText = (EditText) v.findViewById(R.id.HostEditText);
        hostText.addTextChangedListener(loginWatcher);
        hostText.addTextChangedListener(registerWatcher);

        portText = (EditText) v.findViewById(R.id.PortEditText);
        portText.addTextChangedListener(loginWatcher);
        portText.addTextChangedListener(registerWatcher);

        usernameText = (EditText) v.findViewById(R.id.UsernameEditText);
        usernameText.addTextChangedListener(loginWatcher);
        usernameText.addTextChangedListener(registerWatcher);

        passwordText = (EditText) v.findViewById(R.id.PasswordEditText);
        passwordText.addTextChangedListener(loginWatcher);
        passwordText.addTextChangedListener(registerWatcher);

        firstNameText = (EditText) v.findViewById(R.id.FirstNameEditText);
        firstNameText.addTextChangedListener(loginWatcher);
        firstNameText.addTextChangedListener(registerWatcher);

        lastNameText = (EditText) v.findViewById(R.id.LastNameEditText);
        lastNameText.addTextChangedListener(loginWatcher);
        lastNameText.addTextChangedListener(registerWatcher);

        emailText = (EditText) v.findViewById(R.id.EmailEditText);
        emailText.addTextChangedListener(loginWatcher);
        emailText.addTextChangedListener(registerWatcher);

        maleBox = (RadioButton) v.findViewById(R.id.isMale);
        femaleBox = (RadioButton) v.findViewById(R.id.isFemale);

        maleBox.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(registerChecker() && buttonChecker())
                {
                    registerButton.setEnabled(true);
                }
                else
                {
                    registerButton.setEnabled(false);
                }
            }
        });
        femaleBox.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(registerChecker() && buttonChecker())
                {
                    registerButton.setEnabled(true);
                }
                else
                {
                    registerButton.setEnabled(false);
                }
            }
        });


        loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                login();
            }
        });

        registerButton = (Button) v.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                register();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private boolean loginChecker()
    {
        return StringChecker(false);
    }

    private boolean registerChecker()
    {
        return StringChecker(true);
    }

    @Override
    public void firstStage(String[] result)
    {
        if(result[1] == null)
        {
            Toast.makeText(getActivity(), result[0], Toast.LENGTH_SHORT).show();
        }
        else
        {
            DataTask dataTask = new DataTask(listener);

            dataTask.execute(result[0], result[1]);
        }
    }

    @Override
    public void secondStage(String toastMessage)
    {
        //Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void onCheckMale()
    {
        if(femaleBox.isChecked())
        {
            femaleBox.toggle();
        }

    }

    private void onCheckFemale()
    {
        if(maleBox.isChecked())
        {
            maleBox.toggle();
        }
    }

    private void register()
    {
        if(!StringChecker(true))
        {
            Toast.makeText(getActivity(), "Missing info", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String gender = "";
        if(maleBox.isChecked())
        {
            gender = "m";
        }
        if(femaleBox.isChecked())
        {
            gender = "f";
        }



        RegisterRequest request = new RegisterRequest();

        request.setUserName(userName);
        request.setPassword(password);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setGender(gender);

        RegisterTask task = new RegisterTask(this);
        task.execute(request);

    }

    private void login()
    {
        if(!StringChecker(false))
        {
            Toast.makeText(getActivity(), "Missing info", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        LoginRequest request = new LoginRequest();

        request.setUserName(userName);
        request.setPassword(password);

        LoginTask task = new LoginTask(this);
        task.execute(request);

    }

    private boolean StringChecker(boolean register)
    {
        String host = hostText.getText().toString();
        String port = portText.getText().toString();
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(host.equals("") || port.equals("") || userName.equals("") || password.equals(""))
        {
            return false;
        }
        if(register)
        {
            String firstName = firstNameText.getText().toString();
            String lastName = lastNameText.getText().toString();
            String email = emailText.getText().toString();
            if(!maleBox.isChecked() && !femaleBox.isChecked())
            {
                return false;
            }
            if(firstName.equals("") || lastName.equals("") || email.equals(""))
            {
                return false;
            }
        }

        ServerProxy.getInstance().setHostName(host);
        ServerProxy.getInstance().setPortNumber(Integer.parseInt(port));

        return true;
    }

}