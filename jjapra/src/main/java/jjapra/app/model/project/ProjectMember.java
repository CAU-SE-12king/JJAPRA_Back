package jjapra.app.model.project;

import jakarta.persistence.*;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.Role;
import lombok.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@DependsOn(value={"Project", "Account"})
@Getter
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Role role;

}
