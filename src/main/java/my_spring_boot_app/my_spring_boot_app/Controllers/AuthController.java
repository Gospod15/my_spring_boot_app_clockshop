package my_spring_boot_app.my_spring_boot_app.Controllers;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "Admin/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "Admin/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") AppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register?error";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setRole("USER");

        userRepository.save(user);

        return "redirect:/login?success";
    }
}