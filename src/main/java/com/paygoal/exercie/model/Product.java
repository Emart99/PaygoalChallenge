package com.paygoal.exercie.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Schema(description = "Entidad Producto")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del producto", example = "1")
    private Long id;

    @NotBlank(message = "Name is required")
    @Schema(description = "Nombre del producto", example = "Silla de madera", required = true)
    private String name;

    @Schema(description = "Descripcion detallada del producto", example = "Una silla comoda")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Precio del producto", example = "9999", required = true)
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Disponibilidad de stock", example = "10", minimum = "0")
    private int stock;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}
}