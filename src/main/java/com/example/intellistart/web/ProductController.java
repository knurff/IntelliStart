package com.example.intellistart.web;

import com.example.intellistart.model.Product;
import com.example.intellistart.model.User;
import com.example.intellistart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }


    @GetMapping("/products")
    public CollectionModel<EntityModel<Product>> all() {

        List<EntityModel<Product>> products = service.getProducts();

        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).all()).withSelfRel());
    }

    @PostMapping("/products")
    Product newProduct(@Valid @RequestBody Product newProduct) {
        return service.save(newProduct);
    }

    @GetMapping("/products/{id}")
    public EntityModel<Product> one(@PathVariable Long id) {
        return service.getOneProduct(id);
    }

    @PutMapping("/products/{id}")
    Product replaceProduct(@Valid @RequestBody Product newProduct, @PathVariable Long id) {
        return service.getProduct(newProduct, id);
    }

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/products/{id}/buy-by")
    CollectionModel<EntityModel<User>> allPurchasedProducts(@PathVariable Long id) {
        List<EntityModel<User>> users = service.getUsers(id);

        return CollectionModel.of(users, linkTo(methodOn(ProductController.class).all()).withSelfRel());
    }
}
