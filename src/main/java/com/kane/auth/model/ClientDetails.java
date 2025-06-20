package com.kane.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "client_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "grant_types")
    private String grantTypes;

    private String scopes;

    private String resources;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column(name = "secret_required")
    private Boolean secretRequired;
}
