package com.paygoal.exercie.utils;

import com.paygoal.exercie.dto.ProductDto;
import com.paygoal.exercie.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "id", target = "id")
    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);

    @Mapping(source = "id", target = "id")
    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);
}