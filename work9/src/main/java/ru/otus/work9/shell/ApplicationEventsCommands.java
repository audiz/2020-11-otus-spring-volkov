package ru.otus.work9.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.work9.service.AuthorService;
import ru.otus.work9.service.BookService;
import ru.otus.work9.service.CommentService;
import ru.otus.work9.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final CommentService commentService;

    @ShellMethod(value = "List comments", key = {"lc", "list_comments"})
    public String listComments(@ShellOption(value = "--id", defaultValue = "0") Long id) {
        return commentService.listComments(id);
    }

    @ShellMethod(value = "List books", key = {"lb", "list_books"})
    public String listBooks() {
        return bookService.listBooks();
    }

    @ShellMethod(value = "List authors", key = {"la", "list_authors"})
    public String listAuthors() {
        return authorService.listAuthors();
    }

    @ShellMethod(value = "List genres", key = {"lg", "list_genres"})
    public String listGenres() {
        return genreService.listGenres();
    }


    @ShellMethod(value = "Add genre", key = {"ig", "insert_genre"})
    public String insertGenre(@ShellOption("--name") String name) {
       return genreService.insertGenre( name);
    }

    @ShellMethod(value = "Add author", key = {"ia", "insert_author"})
    public String insertAuthor(@ShellOption("--name") String name) {
        return authorService.insertAuthor(name);
    }

    @ShellMethod(value = "Add book", key = {"ib", "insert_book"})
    public String insertBook(@ShellOption("--title") String title, @ShellOption("--genre") Long genre, @ShellOption("--author") Long author) {
        return bookService.insertBook(title, genre, author);
    }

    @ShellMethod(value = "Add comment", key = {"ic", "insert_comment"})
    public String insertComment(@ShellOption("--id_book") Long id, @ShellOption("--comment") String comment) {
        return commentService.insertComment(id, comment);
    }


    @ShellMethod(value = "Update genre", key = {"ug", "update_genre"})
    public String updateGenre(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        return genreService.updateGenre(id, name);
    }

    @ShellMethod(value = "Update author", key = {"ua", "update_author"})
    public String updateAuthor(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        return authorService.updateAuthor(id, name);
    }

    @ShellMethod(value = "Update book", key = {"ub", "update_book"})
    public String updateBook(@ShellOption("--id") Long id, @ShellOption("--name") String title, @ShellOption("--genre") Long genre, @ShellOption("--author") Long author) {
       return bookService.updateBook(id, title, genre, author);
    }

    @ShellMethod(value = "Update comment", key = {"uc", "update_comment"})
    public String updateComment(@ShellOption("--id") Long id, @ShellOption("--comment") String comment) {
        return commentService.updateComment(id, comment);
    }


    @ShellMethod(value = "Get genre", key = {"gg", "get_genre"})
    public String getGenre(@ShellOption("--id") Long id) {
        return genreService.getGenre(id);
    }

    @ShellMethod(value = "Get author", key = {"ga", "get_author"})
    public String getAuthor(@ShellOption("--id") Long id) {
       return authorService.getAuthor(id);
    }

    @ShellMethod(value = "Get book", key = {"gb", "get_book"})
    public String getBook(@ShellOption("--id") Long id) {
       return bookService.getBook(id);
    }


    @ShellMethod(value = "Delete book", key = {"db", "delete_book"})
    public String deleteBook(@ShellOption("--id") Long id) {
        return bookService.deleteBook(id);
    }

    @ShellMethod(value = "Delete author", key = {"da", "delete_author"})
    public String deleteAuthor(@ShellOption("--id") Long id) {
        return authorService.deleteAuthor(id);
    }

    @ShellMethod(value = "Delete genre", key = {"dg", "delete_genre"})
    public String deleteGenre(@ShellOption("--id") Long id) {
        return genreService.deleteGenre(id);
    }

    @ShellMethod(value = "Delete comment", key = {"dc", "delete_comment"})
    public String deleteComment(@ShellOption("--id") Long id) {
        return commentService.deleteComment(id);
    }
}
