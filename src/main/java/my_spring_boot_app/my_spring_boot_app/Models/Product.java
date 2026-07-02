package my_spring_boot_app.my_spring_boot_app.Models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal price;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    private String image;

    private double rate;
    private boolean isFavorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String name, BigDecimal price, String description, String image, double rate, boolean isFavorite) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.rate = rate;
        this.isFavorite = isFavorite;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    //GETTERS

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getRate() {
        return rate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Category getCategory() {
        return category;
    }

    //SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}