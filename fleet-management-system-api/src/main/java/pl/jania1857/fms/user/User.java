package pl.jania1857.fms.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.jania1857.fms.assignment.Assignment;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue
    private long id;

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "driver_id")
    List<Assignment> assignments;

    boolean wasLoggedIn = false;
    boolean changedPassword = false;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "";
    }
}
