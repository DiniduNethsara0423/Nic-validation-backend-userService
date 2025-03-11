package mobios.crm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignupRequestDto {
    private String username;
    private String email;
    private String password;
}
