package my_spring_boot_app.my_spring_boot_app.Controllers;

import my_spring_boot_app.my_spring_boot_app.Models.ItemCart;
import my_spring_boot_app.my_spring_boot_app.Repositories.ItemCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ItemCartRepository itemCartRepository;

    @ModelAttribute
    public void addCartAttributes(Model model) {
        Iterable<ItemCart> cartItems = itemCartRepository.findAll();

        int count = 0;
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCart item : cartItems) {
            count += item.getQuantity();
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(itemTotal);
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartCount", count);
        model.addAttribute("cartTotal", total);
    }
}