package gspot.com.sportify.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gspot.com.sportify.R;
import gspot.com.sportify.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    /*Code for when the User requests sign up*/
    private static final int REQUEST_SIGNUP = 0;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated User */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;

    /*Dialog box */
    ProgressDialog progressDialog;


    /*link to the widgets*/
    @Bind(R.id.input_email) EditText mEmailText;
    @Bind(R.id.input_password) EditText mPasswordText;
    @Bind(R.id.btn_login) Button mLoginButton;
    @Bind(R.id.link_signup) TextView mSignupText;

    /* onClick()
     * Annotation listener for the login button
     * Once the button is clicked the login() is called
     * to log the User i
     * */
    @OnClick(R.id.btn_login)
    void onClick(Button button) { login(); }

    /* onClick()
     * Annotation listener for the sign up link
     * Once the link is clicked the signup activity is started
     * */
    @OnClick(R.id.link_signup)
    void onClick () {
        Log.i(TAG, "onClick for signup");
        /*create an intent to start the activity*/
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);

        /*expecting data to be returned by SignupActivity*/
        startActivityForResult(intent, REQUEST_SIGNUP);
    }//end onClick()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        setContentView(R.layout.activity_login);

        /*link the widgets to the members*/
        ButterKnife.bind(this);

    } //end onCreate()

    /* onActivityResult()
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     *  @param requestCode: The integer request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode:  the integer result code returned by the child activity through its setResult().
     * @param data: the result data from the caller, call the correct getExtra method to get the data*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult()");

        if(resultCode != Activity.RESULT_OK){  /*something bad happened*/
            Log.d(TAG, "RESULT CANCELED!!");
            return;
        } //end if

        if (requestCode == REQUEST_SIGNUP) {
            /*empty intent passed in*/
            //TODO this needs to be verified later
            //if(data == null) return;

            // TODO: Implement successful signup logic here
            // By default we just finish the Activity and log them in automatically
            Intent intent = new Intent(getApplicationContext(), SportListActivity.class);
            startActivity(intent);
            finish();
        } //end if

    } //end onActivityResult

    /*
    * onBackPressed() when the User presses the back button this function will be called
    * this function will disable that button and prevent the User from killing the app*/
    @Override
    public void onBackPressed() {
        //Move the task containing this activity to the back of the activity stack.
        // disable going back to the MainActivity
        moveTaskToBack(true);
    } //end onBackPressed()

    /* login()
     * utility function to log the User in and retrieve their information
     * */
    private void login() {
        Log.d(TAG, "Login");

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        //Checks user input
        if(email.equals(""))
        {
            mEmailText.setError("Please enter a valid email");
            return;
        }

        //Checks user input
        if(password.equals(""))
        {
            mPasswordText.setError("Please enter a valid password");
            return;
        }

        //Show dialog box
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating..");
        progressDialog.show();

        //Authenticate user input through firebase
        mFirebaseRef.authWithPassword(email, password,
                new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));

    } //end login()

    //Authenticator class
    private class MyAuthResultHandler implements Firebase.AuthResultHandler
    {
        private final String provider;
        public MyAuthResultHandler(String provider)
        {
           this.provider = provider;
        }

        //Successful Login
        @Override
        public void onAuthenticated(AuthData authData)
        {
            progressDialog.dismiss();
            Log.i(TAG, provider + " " + "SUCCESSFUL LOGIN");


            if(authData != null)
            {
                //Goes to the SportsList page
                Intent intent = new Intent(LoginActivity.this, SportListActivity.class);
                startActivity(intent);
                finish();
            }

        }

        /*Function name : onAuthetication Error(FirebaseError firebaseError)
          Description : If user login fails this program will check what error
          caused it to fail and display to the user
         */
        @Override
        public void onAuthenticationError(FirebaseError firebaseError)
        {
            //Gets rid of the dialog box
           progressDialog.dismiss();
            Log.i(TAG, provider + " " + "UNCESSFUL LOGIN");

            //Error case
            switch(firebaseError.getCode())
            {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEmailText.setError("Invalid email");
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mPasswordText.setError("Wrong Password");
                    break;
                case FirebaseError.NETWORK_ERROR:
                    showErrorToast("No Network connection , please your connection and " +
                            "try again ");
                    break;
                default:
                    showErrorToast(firebaseError.toString());

            }
        }
    }

    private void showErrorToast(String message)
    {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy()
    {//Called when the view hierarchy associated with the Activity is being removed.
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        ButterKnife.unbind(this);
    }

}
