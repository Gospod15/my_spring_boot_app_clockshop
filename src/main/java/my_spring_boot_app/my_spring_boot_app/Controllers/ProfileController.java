package my_spring_boot_app.my_spring_boot_app.Controllers;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Models.Order;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    private final AppUserRepository userRepository;
    private final OrderRepository orderRepository;

    public ProfileController(AppUserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/profile")
    public String userProfile(Principal principal, Model model) {
        String username = principal.getName();
        AppUser user = userRepository.findByUsername(username).orElseThrow();

        model.addAttribute("user", user);

        List<Order> userOrders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        model.addAttribute("userOrders", userOrders);

        String role = user.getRole();

        if (role != null && (role.contains("ADMIN") || role.contains("MODERATOR"))) {
            List<Order> allOrders = orderRepository.findAll();
            model.addAttribute("allOrders", allOrders);
        }

        if (role != null && role.contains("ADMIN")) {
            List<AppUser> allUsers = userRepository.findAll();
            model.addAttribute("allUsers", allUsers);
        }

        return "Admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String address,
                                @RequestParam(required = false) String phone,
                                Principal principal) {
        String username = principal.getName();
        AppUser user = userRepository.findByUsername(username).orElseThrow();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);

        if (phone != null) {
            user.setPhone(phone);
        }

        userRepository.save(user);

        return "redirect:/profile?success";
    }
}