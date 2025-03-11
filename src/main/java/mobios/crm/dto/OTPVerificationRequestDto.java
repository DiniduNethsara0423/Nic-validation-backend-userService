package mobios.crm.dto;

import lombok.Data;

@Data
public class OTPVerificationRequestDto {
    private String email;
    private String otp;
}
