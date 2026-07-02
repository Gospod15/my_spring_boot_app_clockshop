package my_spring_boot_app.my_spring_boot_app.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import my_spring_boot_app.my_spring_boot_app.Models.ItemCart;
import my_spring_boot_app.my_spring_boot_app.Models.Product;
import my_spring_boot_app.my_spring_boot_app.Repositories.ItemCartRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ItemCartRepository itemCartRepository;

    @Autowired
    private ProductRepository productRepository;

    private String redirectBack(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    // ДОДАВАННЯ ТОВАРУ
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpServletRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не знайдено"));

        ItemCart existingItem = itemCartRepository.findByProduct(product);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            itemCartRepository.save(existingItem);
        } else {
            ItemCart newItem = new ItemCart(product, quantity);
            itemCartRepository.save(newItem);
        }

        return redirectBack(request);
    }

    // ВИДАЛЕННЯ ТОВАРУ
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId, HttpServletRequest request) {
        if (itemCartRepository.existsById(cartItemId)) {
            itemCartRepository.deleteById(cartItemId);
        }
        return redirectBack(request);
    }

    // ОЧИЩЕННЯ КОШИКА
    @PostMapping("/clear")
    public String clearCart(HttpServletRequest request) {
        itemCartRepository.deleteAll();
        return redirectBack(request);
    }

    @PostMapping("/update")
    public String updateCartQuantity(@RequestParam("cartItemId") Long cartItemId,
                                     @RequestParam("quantity") int quantity,
                                     HttpServletRequest request) {
        ItemCart item = itemCartRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            if (quantity > 0) {
                item.setQuantity(quantity);
                itemCartRepository.save(item);
            } else {
                itemCartRepository.delete(item);
            }
        }
        return redirectBack(request);
    }
}