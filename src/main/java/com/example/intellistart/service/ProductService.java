package com.example.intellistart.service;

import com.example.intellistart.data.ProductRepository;
import com.example.intellistart.model.Product;
import com.example.intellistart.model.User;
import com.example.intellistart.web.ProductController;
import com.example.intellistart.web.UserController;
import com.example.intellistart.web.exceptions.ProductNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<EntityModel<Product>> getProducts() {
        return repository.findAll().stream()
                .map(product -> EntityModel.of(product,
                        linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel(),
                        linkTo(methodOn(ProductController.class).all()).withRel("products")))
                .collect(Collectors.toList());
    }

    public Product save(Product newProduct) {
        return repository.save(newProduct);
    }

    public EntityModel<Product> getOneProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).one(id)).withSelfRel(),
                linkTo(methodOn(ProductController.class).all()).withRel("products"));
    }

    public Product getProduct(Product newProduct, Long id) {
        return repository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    return repository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<EntityModel<User>> getUsers(Long id) {
        return repository.findById(id).stream()
                .map(Product::getBoughtBy)
                .flatMap(Collection::stream)
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).all()).withRel("users")))
                .collect(Collectors.toList());
    }
}
