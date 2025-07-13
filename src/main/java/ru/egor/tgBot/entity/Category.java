package ru.egor.tgBot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
}
