package my_spring_boot_app.my_spring_boot_app.Controllers;

import my_spring_boot_app.my_spring_boot_app.Models.AppUser;
import my_spring_boot_app.my_spring_boot_app.Models.Category;
import my_spring_boot_app.my_spring_boot_app.Models.Product;
import my_spring_boot_app.my_spring_boot_app.Repositories.AppUserRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.CategoryRepository;
import my_spring_boot_app.my_spring_boot_app.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/admin")
    public String admin(@RequestParam(value = "categoryId", required = false) Long categoryId, Model model) {
        Iterable<Category> categories = categoryRepository.findAll();

        model.addAttribute("title", "Панель адміністратора");
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);

        if (categoryId != null) {
            model.addAttribute("products", productRepository.findByCategoryId(categoryId));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }

        return "Admin/admin";
    }


    @GetMapping("/addproduct")
    public String addProductForm(Model model) {
        model.addAttribute("title", "Додати новий товар");
        model.addAttribute("type", "product");
        model.addAttribute("actionUrl", "/admin/products/save");
        model.addAttribute("item", new Product());
        model.addAttribute("categories", categoryRepository.findAll());

        return "Admin/universal-form";
    }

    @GetMapping("/edit")
    public String editProductForm(@RequestParam(value = "productId", required = true) Long productId, Model model) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не знайдено"));

        model.addAttribute("title", "Редагування товару: " + product.getName());
        model.addAttribute("type", "product");
        model.addAttribute("actionUrl", "/admin/products/save");
        model.addAttribute("item", product);
        model.addAttribute("categories", categoryRepository.findAll());

        return "Admin/universal-form";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(@ModelAttribute("item") Product product,
                              @RequestParam("category.id") Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow();
        product.setCategory(category);

        productRepository.save(product);
        return "redirect:/admin";
    }

    @GetMapping("/remove")
    public String removeProduct(@RequestParam(value = "productId", required = true) Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("title", "Створення нової категорії");
        model.addAttribute("type", "category");
        model.addAttribute("actionUrl", "/admin/categories/save");
        model.addAttribute("item", new Category());

        return "Admin/universal-form";
    }

    @GetMapping("/admin/categories/edit/{id}")
    public String editCategoryForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категорію не знайдено"));

        model.addAttribute("title", "Редагування категорії: " + category.getName());
        model.addAttribute("type", "category");
        model.addAttribute("actionUrl", "/admin/categories/save");
        model.addAttribute("item", category);

        return "Admin/universal-form";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@ModelAttribute("item") Category category) {
        categoryRepository.save(category);
        return "redirect:/admin";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категорію не знайдено"));

        categoryRepository.delete(category);
        return "redirect:/admin";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Користувача не знайдено"));

        model.addAttribute("title", "Редагування користувача: " + user.getUsername());
        model.addAttribute("type", "user"); // Кажемо формі, що це людина
        model.addAttribute("actionUrl", "/admin/users/save");
        model.addAttribute("item", user);

        return "Admin/universal-form";
    }

    @PostMapping("/admin/users/save")
    public String saveEditedUser(@ModelAttribute("item") AppUser formUser) {
        AppUser existingUser = appUserRepository.findById(formUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Користувача не знайдено"));

        existingUser.setUsername(formUser.getUsername());
        existingUser.setFirstName(formUser.getFirstName());
        existingUser.setLastName(formUser.getLastName());
        existingUser.setEmail(formUser.getEmail());
        existingUser.setPhone(formUser.getPhone());
        existingUser.setAddress(formUser.getAddress());
        existingUser.setRole(formUser.getRole());

        appUserRepository.save(existingUser);

        return "redirect:/profile";
    }
}