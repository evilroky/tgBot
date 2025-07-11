package ru.egor.tgBot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    @Column(nullable = false,unique = true,length = 50)
    private String name;

    @Column(nullable = false,length = 400)
    private String description;

    @Column(nullable = false)
    private double price;
}
