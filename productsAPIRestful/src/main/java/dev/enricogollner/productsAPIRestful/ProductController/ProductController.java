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
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productsList = productRepository.findAll();

        if (!productsList.isEmpty()) {
            for(ProductModel product : productsList) {
                UUID productId = product.getId();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(productId)).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(productsList);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found to update.");
        }

        var productModel = product0.get();  // auxiliar para atualizar sem gerar um novo id
        BeanUtils.copyProperties(productRecordDTO, productModel);  // atualizando SOMENTE campos que foram alterados do dto para o model

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID productId) {
        Optional<ProductModel> product0 = productRepository.findById(productId);

        if (product0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found to delete.");
        }

        productRepository.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

}
