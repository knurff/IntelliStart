package com.example.intellistart.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;

    @NotNull(message = "First name must be not null")
    protected String firstName;

    @NotNull(message = "Last name must be not null")
    protected String lastName;

    @Min(value = 0, message = "Amount of money must be greater than zero")
    protected double amountOfMoney;

    @ManyToMany(mappedBy = "boughtBy")
    @ToString.Exclude
    protected List<Product> purchasedProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    public void cashOut(double price){
        amountOfMoney -= price;
    }
    public void addProduct(Product product){
        purchasedProducts.add(product);
        product.getBoughtBy().add(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
