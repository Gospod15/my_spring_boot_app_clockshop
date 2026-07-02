package my_spring_boot_app.my_spring_boot_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Додано /order/submit та /order/success
                        .requestMatchers("/", "/checkout", "/about", "/cart/add", "/cart/remove", "/cart/update",
                                "/catalog", "/categories", "/product/**", "/order/submit", "/order/success",
                                "/register", "/404", "/error/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // мають доступ тільки ADMIN та MODERATOR
                        .requestMatchers("/admin/**", "/addproduct", "/edit", "/remove", "/admin/orders/**").hasAnyRole("ADMIN", "MODERATOR")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}