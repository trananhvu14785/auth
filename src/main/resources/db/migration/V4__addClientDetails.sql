INSERT INTO client_details (
    client_id,
    client_secret,
    grant_types,
    scopes,
    resources,
    redirect_uris,
    access_token_validity,
    refresh_token_validity,
    secret_required
) VALUES (
             'test-client',
             '123456',
             'password,refresh_token',
             'read,write',
             'resource-server',
             'http://localhost:3000/login/oauth2/code/test-client',
             900,
             604800,
             TRUE
         );
