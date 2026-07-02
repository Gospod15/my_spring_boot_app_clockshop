package my_spring_boot_app.my_spring_boot_app.Controllers;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Models.ItemCart;
import my_spring_boot_app.my_spring_boot_app.Models.Order;
import my_spring_boot_app.my_spring_boot_app.Models.OrderItem;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.ItemCartRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final ItemCartRepository itemCartRepository;
    private final AppUserRepository appUserRepository;

    public OrderController(OrderRepository orderRepository, ItemCartRepository itemCartRepository, AppUserRepository appUserRepository) {
        this.orderRepository = orderRepository;
        this.itemCartRepository = itemCartRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/admin/orders/delete")
    public String deleteOrder(@RequestParam("id") Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        }
        return "redirect:/profile";
    }

    @PostMapping("/order/submit")
    public String submitOrder(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phone") String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam("address") String address,
            Principal principal) {

        AppUser user = null;
        if (principal != null) {
            user = appUserRepository.findByUsername(principal.getName()).orElse(null);
        }

        List<ItemCart> cartItems = itemCartRepository.findAll();

        if (cartItems.isEmpty()) {
            return "redirect:/catalog";
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ItemCart item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        String customerFullName = firstName + " " + lastName;
        Order order = new Order(user, customerFullName, email, phone, address, LocalDateTime.now(), totalAmount, "NEW");

        for (ItemCart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice());
            order.addItem(orderItem);
        }

        orderRepository.save(order);

        itemCartRepository.deleteAll(cartItems);

        return "redirect:/order/success";
    }

    @Transactional
    @PostMapping("/admin/orders/updateStatus")
    public ResponseEntity<Void> updateOrderStatus(@RequestParam("orderId") Long orderId,
                                                  @RequestParam("status") String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Замовлення не знайдено"));

        order.setStatus(status);
        orderRepository.save(order);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/success")
    public String orderSuccessPage(Model model) {
        model.addAttribute("title", "Замовлення оформлено");
        return "order-success";
    }
}