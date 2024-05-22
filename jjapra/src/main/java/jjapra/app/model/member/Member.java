package jjapra.app.model.member;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member {
    @Id
    @Column(nullable = false, length = 30)
    private String id;
    @Column(nullable = false, length = 30)
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    @Column()
    private String email;
    @Column(nullable = false, length = 30)
    private String phone_num;
    @Column(nullable = false)
    private boolean is_admin = false;
}
