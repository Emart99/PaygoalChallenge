package com.paygoal.exercie.controller;

import com.paygoal.exercie.model.Product;
import com.paygoal.exercie.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "API for managing products")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Operation(summary = "Crear un producto nuevo", description = "Crea un producto nuevo y lo retorna")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Introdujo algun dato erroneo",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/BadRequestValidationError"))),
    })
    @PostMapping("/createProduct/")
    public ResponseEntity<Product> add(@Valid @RequestBody Product product) {
        Product created = productService.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Buscar producto por id", description = "Dado un id retorna un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encotnrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/NotFoundError")))
    })
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Product> findById(@Parameter(description = "ID del producto a buscar") @PathVariable Long id) {
        Product producto = productService.findById(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Actualiza un producto ya existente", description = "Actualiza un producto y lo retorna")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Introdujo algun dato erroneo",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/BadRequestValidationError"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/NotFoundError")))
    })
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> update(
            @Parameter(description = "ID del producto a atuallizar") @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {
        Product updated = productService.update(id, productDetails);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Borra un producto", description = "Borra un producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto borrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/NotFoundError")))
    })
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto para borrar") @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Trae todos los productos ordenados por precio", description = "Retorna una lista de productos ordenados por precio ascendentemente")
    @ApiResponse(responseCode = "200", description = "Lista de productos encontrada exitosamente")
    @GetMapping("/findAllOrderedByPrice")
    public ResponseEntity<Iterable<Product>> findAllOrderedByPrice() {
        Iterable<Product> products = productService.findAllOrderedByPrice();
        return ResponseEntity.ok(products);
    }
}