package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.SubscribeRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.SubscribeServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/subscribe")
@RequiredArgsConstructor
public class SubscribeApi {
    private final SubscribeServiceInterface subscribeService;

    @PostMapping
    public ResponseEntity<ApiResponse> subscribe(
            @RequestBody @Valid SubscribeRequest subscribeRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        subscribeService.updateSubscribe(
                subscribeRequest.packageType(), subscribeRequest.subscribeStatus(), userPrincipal.id());
        return ResponseEntity.ok(ApiResponse.of("you're successfully subscribe"));
    }
}
