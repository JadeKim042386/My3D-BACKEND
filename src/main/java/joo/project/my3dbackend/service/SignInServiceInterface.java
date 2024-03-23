package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.response.LoginResponse;

public interface SignInServiceInterface {
    LoginResponse signIn(String email, String password);
}
