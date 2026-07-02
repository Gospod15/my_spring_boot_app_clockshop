package my_spring_boot_app.my_spring_boot_app.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    private String image;

    private boolean recomended;

    public Category() {
    }

    public Category(String name, String description, String image, boolean recomended) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.recomended = recomended;
    }

    public Category(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setRecomended(boolean recomended) {
        this.recomended = recomended;
    }

    public boolean isRecomended() {
        return recomended;
    }
}