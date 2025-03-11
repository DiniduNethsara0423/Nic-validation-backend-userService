package mobios.crm.controller;

import lombok.RequiredArgsConstructor;
import mobios.crm.dto.*;
import mobios.crm.service.UserService;
import mobios.crm.service.impl.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

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
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        String response = userService.logout(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        String response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/send-otp/{email}")
    public ResponseEntity<String> sendOtp(@PathVariable("email") String email) {
        String response = userService.sendOtp(email);
        return ResponseEntity.ok(response);
    }

}
