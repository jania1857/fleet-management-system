package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jania1857.fmsapi.dto.publicdto.AuthenticationRequest;
import pl.jania1857.fmsapi.dto.publicdto.AuthenticationResponse;
import pl.jania1857.fmsapi.service.utils.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public")
public class PublicController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
