package com.example.intellistart.service;

import com.example.intellistart.data.ProductRepository;
import com.example.intellistart.data.UserRepository;
import com.example.intellistart.model.Product;
import com.example.intellistart.model.User;
import com.example.intellistart.web.ProductController;
import com.example.intellistart.web.UserController;
import com.example.intellistart.web.exceptions.InsufficientUserBalanceException;
import com.example.intellistart.web.exceptions.ProductNotFoundException;
import com.example.intellistart.web.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {
    private final UserRepository repository;
    private final ProductRepository productRepository;

    @Autowired
    public UserService(UserRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public List<EntityModel<User>> getUsers() {
        return repository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).all()).withRel("users")))
                .collect(Collectors.toList());
    }

    public User save(User newUser) {
        return repository.save(newUser);
    }

    public EntityModel<User> getOneUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }

    public User replaceUser(User newUser, Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setAmountOfMoney(newUser.getAmountOfMoney());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<EntityModel<Product>> getProducts(Long id) {
        return repository.findById(id).stream()
                .map(User::getPurchasedProducts)
                .flatMap(Collection::stream)
                .map(product -> EntityModel.of(product,
                        linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel(),
                        linkTo(methodOn(ProductController.class).all()).withRel("products")))
                .collect(Collectors.toList());
    }

    public User buy(Long userId, Long productId) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if (user.getAmountOfMoney() < product.getPrice()) throw new InsufficientUserBalanceException(userId, productId);
        else {
            user.addProduct(product);
            user.setAmountOfMoney(user.getAmountOfMoney() - product.getPrice());
            repository.save(user);
            return user;
        }
    }
}
