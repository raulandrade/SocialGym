package socialgym.dce.ufpb.br.socialgym;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import java.util.Arrays;


public class LoginActivity extends Activity {

    public static final String EMAIL_PERMISSION = "email";
    public static final String PUBLIC_PROFILE_PERMISSION = "public_profile";
    public static final String USER_FRIENDS_PERMISSION = "user_friends";

    private UiLifecycleHelper uiLifecycleHelper;

    private TextView nomeTextView;
//    private TextView emailTextView;
//    private TextView idTextView;
//    private ProfilePictureView facebookPictureView;
    private LoginButton facebookLoginButton;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChanged(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        this.uiLifecycleHelper = new UiLifecycleHelper(this, callback);
        this.uiLifecycleHelper.onCreate(savedInstanceState);

        this.nomeTextView = (TextView) findViewById(R.id.nomeTextView);
//        this.emailTextView = (TextView) findViewById(R.id.emailTextView);
//        this.idTextView = (TextView) findViewById(R.id.idTextView);
//        this.facebookPictureView = (ProfilePictureView) findViewById(R.id.facebookPictureView);

        this.facebookLoginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
//        this.facebookLoginButton.setPublishPermissions(Arrays.asList(EMAIL_PERMISSION,
//               PUBLIC_PROFILE_PERMISSION, USER_FRIENDS_PERMISSION));

        this.facebookLoginButton.setPublishPermissions(Arrays.asList(
                PUBLIC_PROFILE_PERMISSION));
    }

    // METHODS FACEBOOK
    public void onSessionStateChanged(final Session session, SessionState state, Exception exception){
        if(session != null && session.isOpened()){
            Log.i("Script", "Usuário conectado");
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        nomeTextView.setText(user.getFirstName() + " " + user.getLastName());
 //                       emailTextView.setText(user.getProperty(EMAIL_PERMISSION).toString());
//                       idTextView.setText(user.getId());
//                        facebookPictureView.setProfileId(user.getId());
                    }
                }
            }).executeAsync();
        }
        else{
            Log.i("Script", "Usuário não conectado");
        }
    }
}
