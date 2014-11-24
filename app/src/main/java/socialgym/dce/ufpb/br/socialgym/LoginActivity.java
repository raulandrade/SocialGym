package socialgym.dce.ufpb.br.socialgym;

import java.util.Arrays;
import java.util.List;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class LoginActivity extends Activity {
    public static final String EMAIL_PERMISSION = "email";
    public static final String PUBLIC_PROFILE_PERMISSION = "public_profile";
    public static final String USER_FRIENDS_PERMISSION = "user_friends";

    private TextView nomeTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private ProfilePictureView facebookPictureView;
    private LoginButton loginButton;

    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChanged(session, state, exception);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        this.loginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
        this.loginButton.setPublishPermissions(Arrays.asList(EMAIL_PERMISSION, PUBLIC_PROFILE_PERMISSION, USER_FRIENDS_PERMISSION));

        this.nomeTextView = (TextView) findViewById(R.id.nomeTextView);
        this.emailTextView = (TextView) findViewById(R.id.emailTextView);
        this.idTextView = (TextView) findViewById(R.id.idTextView);
        this.facebookPictureView = (ProfilePictureView) findViewById(R.id.facebookPictureView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null && (session.isClosed() || session.isOpened())) {
            onSessionStateChanged(session, session.getState(), null);
        }
        uiHelper.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


    // METHODS FACEBOOK
    public void onSessionStateChanged(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            Log.i("Script", "Usuário conectado");
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {

                        nomeTextView.setText(user.getFirstName() + " " + user.getLastName());
                        emailTextView.setText(user.getProperty("email").toString());
                        idTextView.setText(user.getId());
                        facebookPictureView.setProfileId(user.getId());

                        getFriends(session);
                    }
                }
            }).executeAsync();
        } else {
            Log.i("Script", "Usuário não conectado");
        }
    }


    public void getFriends(Session session) {
        Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {
                if (users != null) {
                    Log.i("Script", "Friends: " + users.size());
                }
                Log.i("Script", "response: " + response);
            }
        }).executeAsync();
    }
}