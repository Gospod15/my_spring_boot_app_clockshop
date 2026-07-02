package my_spring_boot_app.my_spring_boot_app.Repositories;

import my_spring_boot_app.my_spring_boot_app.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId);

}