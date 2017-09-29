package sinfo.ufrn.br.japi.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import sinfo.ufrn.br.japi.constants.Parameters;
import sinfo.ufrn.br.japi.constants.Paths;
import sinfo.ufrn.br.japi.constants.Urls;
import sinfo.ufrn.br.japi.constants.Values;
import sinfo.ufrn.br.japi.interfaces.AsyncResponse;


/**
 * Created by victor on 28/09/17.
 */

public class AuthorizationTask extends AsyncTask<String, Void, OAuthResponse> {

    private Activity mActivity;
    private Class mClass;
    private ProgressDialog mProgressDialog;

    public AsyncResponse delegate = null;

    public AuthorizationTask(Activity activity, Class mClass) {
        this.mActivity = activity;
        this.mClass = mClass;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = ProgressDialog.show(mActivity, "Japi Authorization", "Carregando...", true);
    }

    @Override
    protected OAuthResponse doInBackground(String... params) {
        try {
            String url = params[0] + Paths.PATH_TOKEN;
            String clientId = params[1];
            String clientSecret = params[2];
            String code = params[3];

            OAuth2Client client;
            Map<String, String> map = new HashMap<>();
            map.put(Parameters.REDIRECT_URI_PARAM, Urls.URL_SINFO);
            map.put(Values.RESPONSE_TYPE_VALUE, code);

            client = new OAuth2Client.Builder(clientId, clientSecret, url)
                    .grantType(Values.GRANT_TYPE)
                    .parameters(map)
                    .build();

            OAuthResponse response = client.requestAccessToken();
            if (response.isSuccessful()) {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(OAuthResponse result) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        delegate.processAuthorization(result);
        mActivity.startActivity(new Intent(mActivity, mClass));
        mActivity.finish();
    }

}
