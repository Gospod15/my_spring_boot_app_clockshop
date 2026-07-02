package my_spring_boot_app.my_spring_boot_app.Repositories;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Models.Favorite;
import my_spring_boot_app.my_spring_boot_app.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(AppUser user);
    void deleteByUserAndProduct(AppUser user, Product product);
    boolean existsByUserAndProduct(AppUser user, Product product);
}