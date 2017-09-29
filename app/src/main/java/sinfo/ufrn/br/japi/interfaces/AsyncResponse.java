package sinfo.ufrn.br.japi.interfaces;

import ca.mimic.oauth2library.OAuthResponse;

/**
 * Created by victor on 28/09/17.
 */

public interface AsyncResponse {

    void processAuthorization(OAuthResponse response);

}
