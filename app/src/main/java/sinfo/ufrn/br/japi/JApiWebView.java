package sinfo.ufrn.br.japi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ca.mimic.oauth2library.OAuthResponse;
import sinfo.ufrn.br.japi.asynctask.AuthorizationTask;
import sinfo.ufrn.br.japi.constants.Generic;
import sinfo.ufrn.br.japi.constants.Parameters;
import sinfo.ufrn.br.japi.constants.Paths;
import sinfo.ufrn.br.japi.constants.Urls;
import sinfo.ufrn.br.japi.constants.Values;
import sinfo.ufrn.br.japi.interfaces.AsyncResponse;

/**
 * Created by victor on 28/09/17.
 */

public class JApiWebView extends WebView implements AsyncResponse {
    private ProgressDialog progressDialog;

    public JApiWebView(Context context) {
        super(context);
        requestFocus(View.FOCUS_DOWN);
    }

    public JApiWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        requestFocus(View.FOCUS_DOWN);
    }

    public JApiWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        requestFocus(View.FOCUS_DOWN);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JApiWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        requestFocus(View.FOCUS_DOWN);
    }

    public JApiWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        requestFocus(View.FOCUS_DOWN);
    }


    public void loadJapiWebView(final String urlBase, final String clientId, final String clientSecret, final Activity myActivity, final Class ativityResult) {
        String url = createUrl(urlBase, clientId);

        progressDialog = ProgressDialog.show(getContext(), "Japi Authorization", "Carregando...", true);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                if (authorizationUrl.startsWith(Urls.URL_SINFO)) {
                    Uri uri = Uri.parse(authorizationUrl);

                    String authorizationCode = uri.getQueryParameter(Values.RESPONSE_TYPE_VALUE);
                    if (authorizationCode == null) {
                        return true;
                    }

                    AuthorizationTask authorizationTask = new AuthorizationTask(myActivity, ativityResult);
                    authorizationTask.delegate = JApiWebView.this;
                    authorizationTask.execute(urlBase, clientId, clientSecret, authorizationCode);
                } else {
                    loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        loadUrl(url);
    }

    private String createUrl(String urlBase, String clientId) {
        return urlBase + Paths.PATH_AUTHORIZATION
                + Generic.QUESTION_MARK
                + Parameters.RESPONSE_TYPE_PARAM + Generic.EQUALS + Values.RESPONSE_TYPE_VALUE
                + Generic.AMPERSAND
                + Parameters.CLIENT_ID_PARAM + Generic.EQUALS + clientId
                + Generic.AMPERSAND
                + Parameters.REDIRECT_URI_PARAM + Generic.EQUALS + Urls.URL_SINFO;
    }


    @Override
    public void processAuthorization(OAuthResponse response) {
        JApi token = new JApi();
        token.savePreferences(response, getContext());
    }
}
