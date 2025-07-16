package com.fest.backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;



@Entity
public class Token {



    @Id
    private String token;
    private boolean revoked;
    private boolean expired;

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
        private  FestUser user;

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public FestUser getUser() {
        return user;
    }

    public void setUser(FestUser festUser) {
        this.user = festUser;
    }



    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
