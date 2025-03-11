package mobios.crm.service.impl;

import lombok.RequiredArgsConstructor;
import mobios.crm.dto.LoginRequestDto;
import mobios.crm.dto.LoginResponseDto;
import mobios.crm.dto.ResetPasswordRequestDto;
import mobios.crm.dto.SignupRequestDto;
import mobios.crm.entity.User;
import mobios.crm.repository.UserRepository;
import mobios.crm.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor // ✅ Ensures all final fields are injected properly
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> loggedInUsers = new HashSet<>();


    @Override
    public String signup(SignupRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        //  Generate OTP
        String otp = emailService.generateOtp();

        // Create user with OTP (not yet verified)
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .otp(otp)
                .verified(false)
                .build();

        userRepository.save(user);

        // Send OTP email
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
        user.setOtp(null); //  Clear OTP after verification
        userRepository.save(user);

        return "Email verified successfully!";
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        Optional<User> userOpt = userRepository.findByEmail(requestDto.getEmail());
        if (userOpt.isEmpty()) {
            return new LoginResponseDto("User not found!");
        }

        User user = userOpt.get();
        if (!user.isVerified()) {
            return new LoginResponseDto("Email not verified!");
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return new LoginResponseDto("Invalid credentials!");
        }

        loggedInUsers.add(user.getEmail()); // ✅ Track logged-in user
        return new LoginResponseDto("Login successful!");
    }
    @Override
    public String logout(String email) {
        if(loggedInUsers.remove(email)){
            return "User Logged Out Succesfully";
        }
        return "User Not Logged Out";
    }

    @Override
    public String resetPassword(ResetPasswordRequestDto requestDto) {
        Optional<User> userOpt = userRepository.findByEmailAndOtp(requestDto.getEmail(), requestDto.getOtp());

        if (userOpt.isEmpty()) {
            return "Invalid OTP!";
        }

        User user = userOpt.get();

        // Check if OTP is expired
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP is invalid or has expired!";
        }
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword())); // Encrypt new password
        user.setOtp(null); // Clear OTP after reset
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Password reset successfully!";
    }

    @Override
    public String sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Email not found!";
        }

        User user = userOpt.get();
        String otp = emailService.generateOtp(); // Generate OTP
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // Set expiry to 5 minutes from now

        // ✅ Ensure expiry time is set
        user.setOtp(otp);
        user.setOtpExpiry(expiryTime);

        System.out.println("Generated OTP: " + otp); // Debugging
        System.out.println("OTP Expiry Time: " + expiryTime); // Debugging

        userRepository.save(user); // Save user with OTP and expiry time
        emailService.sendOtpEmail(user.getEmail(), otp); // Send OTP via email

        return "OTP sent successfully!";
    }


}

