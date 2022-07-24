package com.example.intellistart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;

    @NotNull(message = "Name ust be not null")
    protected String name;

    @Min(value = 0, message = "Price must be greater than zero")
    protected double price;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnore
    protected List<User> boughtBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
