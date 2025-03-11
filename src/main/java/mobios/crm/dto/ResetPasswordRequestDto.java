package mobios.crm.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String email;
    private String newPassword;
    private String otp;

}
