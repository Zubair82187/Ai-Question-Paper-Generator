package com.ai_question_paper_generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookName;
    private String subject;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Chunk> chunks;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}