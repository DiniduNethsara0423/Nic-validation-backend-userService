package mobios.crm.service.impl;

import lombok.RequiredArgsConstructor;
import mobios.crm.dto.SignupRequestDto;
import mobios.crm.entity.User;
import mobios.crm.repository.UserRepository;
import mobios.crm.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // ✅ Ensures all final fields are injected properly
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String signup(SignupRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        // ✅ Generate OTP
        String otp = emailService.generateOtp();

        // ✅ Create user with OTP (not yet verified)
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .otp(otp)
                .verified(false)
                .build();

        userRepository.save(user);

        // ✅ Send OTP email
        emailService.sendOtpEmail(request.getEmail(), otp);
        return "User registered. Please verify OTP sent to your email.";
    }

    @Override
    public String verifyOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmailAndOtp(email, otp);
        if (userOpt.isEmpty()) {
            return "Invalid OTP!";
        }

        User user = userOpt.get();
        user.setVerified(true);
        user.setOtp(null); // ✅ Clear OTP after verification
        userRepository.save(user);

        return "Email verified successfully!";
    }
}
