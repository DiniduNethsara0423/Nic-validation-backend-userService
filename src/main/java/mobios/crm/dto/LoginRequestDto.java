package mobios.crm.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
