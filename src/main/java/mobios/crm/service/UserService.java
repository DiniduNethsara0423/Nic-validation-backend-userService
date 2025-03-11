package mobios.crm.service;

import mobios.crm.dto.SignupRequestDto;
import mobios.crm.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public String signup(SignupRequestDto request) ;
    public String verifyOtp(String email, String otp) ;


    }
