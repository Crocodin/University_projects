package ro.mpp.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mpp.utils.JwtUtils;
import ro.mpp.utils.LoginRequest;
import ro.mpp.utils.LoginResponse;

@RestController
@RequestMapping("fts/auth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final IFestivalService festivalService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Login request received {}", request);
        return festivalService.authenticate(request.username(), request.password())
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getUsername());
                    return ResponseEntity.ok(new LoginResponse(token, user.getUsername()));
                })
                .orElse(ResponseEntity.status(401).build());
    }
}
