package my_spring_boot_app.my_spring_boot_app.Controllers;

import jakarta.transaction.Transactional;
import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Models.Category;
import my_spring_boot_app.my_spring_boot_app.Models.Favorite;
import my_spring_boot_app.my_spring_boot_app.Models.Product;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.CategoryRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.FavoriteRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
public class PageController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    private final FavoriteRepository favoriteRepository;

    public PageController(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          AppUserRepository appUserRepository,
                          FavoriteRepository favoriteRepository) { // Оновіть конструктор
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.appUserRepository = appUserRepository;
        this.favoriteRepository = favoriteRepository;
    }
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Головна сторінка");
        return "index";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        Iterable<Category> categories = categoryRepository.findAll();

        model.addAttribute("title", "Категорії");
        model.addAttribute("categories", categories);

        return "categories";
    }

    @GetMapping("/product")
    public String product(@RequestParam(value = "productId", required = true) Long productId, Model model) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукт не знайдено"));

        model.addAttribute("product", product);
        model.addAttribute("title", product.getName());

        return "product";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Про нас");
        return "about";
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Principal principal,
                                 @RequestParam(value = "categoryId", required = false) Long categoryId,
                                 @RequestParam(value = "showFavorites", required = false) Boolean showFavorites,
                                 Model model) {

        model.addAttribute("title", "Каталог");
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("principal", principal);
        model.addAttribute("showFavorites", showFavorites);

        List<Product> products;

        if (Boolean.TRUE.equals(showFavorites) && principal != null) {
            AppUser user = appUserRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Користувач не знайдений"));

            products = favoriteRepository.findByUser(user).stream()
                    .map(Favorite::getProduct).toList();
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);

        if (principal != null) {
            AppUser user = appUserRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                List<Favorite> favorites = favoriteRepository.findByUser(user);
                model.addAttribute("favoriteProductIds", favorites.stream()
                        .map(f -> f.getProduct().getId()).toList());
            }
        }

        return "catalog";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Principal principal, Model model) {
        if (principal != null) {
            AppUser user = appUserRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("user", user);
        }

        return "checkout";
    }

    @PostMapping("/favorites/add")
    public String addToFavorites(Principal principal, @RequestParam Long productId) {
        AppUser user = appUserRepository.findByUsername(principal.getName()).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        if (!favoriteRepository.existsByUserAndProduct(user, product)) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
        }
        return "redirect:/catalog";
    }

    @Transactional
    @PostMapping("/favorites/remove")
    public String removeFromFavorites(Principal principal, @RequestParam Long productId) {
        AppUser user = appUserRepository.findByUsername(principal.getName()).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        favoriteRepository.deleteByUserAndProduct(user, product);
        return "redirect:/catalog";
    }
}