package com.unbe1iev.common.integration;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;

public abstract class BaseAccessTokenProvider {

    private final Configuration configuration;

    protected BaseAccessTokenProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getBearerToken() {
        AuthzClient client = AuthzClient.create(configuration);
        AccessTokenResponse accessTokenResponse = client.obtainAccessToken();

        return accessTokenResponse.getTokenType() + " " + accessTokenResponse.getToken();
    }
}
