package mobios.crm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String otp;
    private boolean verified;
    private LocalDateTime otpExpiry;

    public void setOtpExpiry(LocalDateTime localDateTime) {
        this.otpExpiry = localDateTime;
    }
}
