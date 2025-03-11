package mobios.crm.controller;

import lombok.RequiredArgsConstructor;
import mobios.crm.dto.OTPVerificationRequestDto;
import mobios.crm.dto.SignupRequestDto;
import mobios.crm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {
        String response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OTPVerificationRequestDto request) {
        String response = userService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(response);
    }

}
