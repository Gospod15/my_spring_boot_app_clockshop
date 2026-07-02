package my_spring_boot_app.my_spring_boot_app.Service;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    public AppUserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole() != null ? appUser.getRole() : "USER")
                .build();
    }
}