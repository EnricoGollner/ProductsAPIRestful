package dev.enricogollner.productsAPIRestful.ProductController;

import dev.enricogollner.productsAPIRestful.DTOs.ProductRecordDTO;
import dev.enricogollner.productsAPIRestful.models.ProductModel;
import dev.enricogollner.productsAPIRestful.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getProducts() {
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.findAll());
    }



    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));  // HttpStatus.CREATED é o 200
    }

}
