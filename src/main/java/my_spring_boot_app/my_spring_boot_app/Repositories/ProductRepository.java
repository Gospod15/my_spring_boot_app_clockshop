package my_spring_boot_app.my_spring_boot_app.Repositories;

import my_spring_boot_app.my_spring_boot_app.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //findAll,delete().....

    Optional<Product> findByName(String name);
    List<Product> findByRate(double rate);
    List<Product> findByCategoryId(long categoryId);
    List<Product> findByIsFavorite(boolean isfavorite);

}
