package com.paygoal.exercie.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de producto para transferencia")
public class ProductDto {

    @Schema(description = "Identificador unico del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Schema(description = "Nombre del producto", example = "Silla de madera", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Descripcion detallada del producto", example = "Una silla comoda")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Precio del producto", example = "9999", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Disponibilidad de stock", example = "10", minimum = "0")
    private int stock;
}

