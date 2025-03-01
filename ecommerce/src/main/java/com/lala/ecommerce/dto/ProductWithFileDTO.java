package com.lala.ecommerce.dto;

import com.lala.ecommerce.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductWithFileDTO {
    private MultipartFile file;
    private Product product;
}