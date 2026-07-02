package my_spring_boot_app.my_spring_boot_app.Repositories;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}