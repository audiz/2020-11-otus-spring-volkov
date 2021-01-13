package ru.otus.work9.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@NamedEntityGraph(name = "author-and-genre-entity-graph", attributeNodes = { @NamedAttributeNode("author"), @NamedAttributeNode("genre") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKS")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(targetEntity = Genre.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "genre", referencedColumnName = "id")
    private Genre genre;

    @ManyToOne(targetEntity = Author.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private Author author;

    @Fetch(FetchMode.SUBSELECT)
    //@OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    //@JoinColumn(name = "book_id")
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    private List<Comment> comments;
}