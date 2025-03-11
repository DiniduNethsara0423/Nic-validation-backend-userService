package mobios.crm.service;

import mobios.crm.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.LoginExceptionResolver;

public interface UserService {

    public String signup(SignupRequestDto request) ;
    public String verifyOtp(String email, String otp) ;

    LoginResponseDto login(LoginRequestDto requestDto);
    String logout(String email);
    String resetPassword(ResetPasswordRequestDto requestDto);


    }
