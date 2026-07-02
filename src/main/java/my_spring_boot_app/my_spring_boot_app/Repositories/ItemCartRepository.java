package my_spring_boot_app.my_spring_boot_app.Repositories;

import my_spring_boot_app.my_spring_boot_app.Models.ItemCart;
import my_spring_boot_app.my_spring_boot_app.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    ItemCart findByProduct(Product product);
}