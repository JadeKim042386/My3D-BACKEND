package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.LoginRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.service.impl.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signin")
@RequiredArgsConstructor
public class SignInApi {
    private final SignInService signInService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody LoginRequest loginRequest) {
        String accessToken = signInService.signIn(loginRequest.email(), loginRequest.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(accessToken));
    }
}
