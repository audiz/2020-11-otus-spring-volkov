/*
package ru.otus.work11.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.work11.domain.Book;
import ru.otus.work11.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import({CommentRepositoryImpl.class})
public class CommentRepositoryTest {

    private static final String COMMENT = "new comment";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен корректно сохранять всю информацию о комментарии")
    @Test
    void shouldSaveAllCommentInfo() {
        Book book = em.find(Book.class, 1L);
        assertThat(book).isNotNull();

        val comment = new Comment(0, COMMENT, book);
        commentRepository.insert(comment);
        assertThat(comment.getId()).isGreaterThan(0);

        val actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment).isNotNull().matches(s -> !s.getComment().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @Test
    void shouldFindExpectedCommentById() {
        val optionalActualComment = commentRepository.findById(1L);
        val expectedComment = em.find(Comment.class, 1L);
        assertThat(optionalActualComment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName(" должен изменять имя заданного комментария по его id")
    @Test
    void shouldUpdateCommentNameById() {
        val firstComment = em.find(Comment.class, 1L);
        String oldComment = firstComment.getComment();
        em.detach(firstComment);
        firstComment.setComment(COMMENT);
        commentRepository.update(firstComment);
        val updatedComment = em.find(Comment.class, 1L);

        assertThat(updatedComment.getComment()).isNotEqualTo(oldComment).isEqualTo(COMMENT);
    }

    @DisplayName(" должен удалять заданный комментарий по его id")
    @Test
    void shouldDeleteCommentById() {
        val deleteComment = em.find(Comment.class, 2L);
        assertThat(deleteComment).isNotNull();

        commentRepository.delete(deleteComment);
        val deletedComment = em.find(Comment.class, 2L);

        assertThat(deletedComment).isNull();
    }

}
*/
