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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID idProduct) {
        Optional<ProductModel> product0 = productRepository.findById(idProduct);

        if (product0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));  // HttpStatus.CREATED é o 200
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable(value = "id") UUID productId,
                                                @RequestBody @Valid ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> product0 = productRepository.findById(productId);

        if (product0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        var productModel = product0.get();  // auxiliar para atualizar sem gerar um novo id
        BeanUtils.copyProperties(productRecordDTO, productModel);  // atualizando SOMENTE campos que foram alterados do dto para o model
        
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

}
