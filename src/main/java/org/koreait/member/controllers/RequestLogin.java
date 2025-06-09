package org.koreait.member.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestLogin {
    private String email;
    private String password;
    private boolean autoLogin;
    private String redirectUrl;
    private List<String> fieldErrors;
    private List<String> globalErrors;
}