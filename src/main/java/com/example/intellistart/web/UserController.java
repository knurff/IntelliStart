package com.example.intellistart.web;

import com.example.intellistart.model.Product;
import com.example.intellistart.model.User;
import com.example.intellistart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = service.getUsers();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping("/users")
    User newUser(@Valid @RequestBody User newUser) {
        return service.save(newUser);
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> one(@PathVariable Long id) {
        return service.getOneUser(id);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@Valid @RequestBody User newUser, @PathVariable Long id) {
        return service.replaceUser(newUser, id);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/users/{id}/purchased-products")
    CollectionModel<EntityModel<Product>> allPurchasedProducts(@PathVariable Long id) {
        List<EntityModel<Product>> products = service.getProducts(id);

        return CollectionModel.of(products, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping("/users/{user-id}/buy-product/{product-id}")
    User buyProduct(@PathVariable("user-id") Long userId, @PathVariable("product-id") Long productId) {
        return service.buy(userId, productId);
    }
}
