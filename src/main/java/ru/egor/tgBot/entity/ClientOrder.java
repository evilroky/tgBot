package ru.egor.tgBot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Client client;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Double total;
}
