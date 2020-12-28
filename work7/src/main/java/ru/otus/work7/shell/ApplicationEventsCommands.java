package ru.otus.work7.shell;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.work7.dao.AuthorDao;
import ru.otus.work7.dao.BookDao;
import ru.otus.work7.dao.GenreDao;
import ru.otus.work7.domain.Author;
import ru.otus.work7.domain.Book;
import ru.otus.work7.domain.Genre;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @ShellMethod(value = "List books", key = {"lb", "list_books"})
    public String listBooks() {
        List<Book> books = bookDao.getAll();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "TITLE", "GENRE", "AUTHOR");
        at.addRule();
        books.forEach(book -> {
            at.addRow(book.getId(), book.getTitle(), book.getGenre().getName(), book.getAuthor().getName());
            at.addRule();
        });
        return at.render();
    }

    @ShellMethod(value = "List authors", key = {"la", "list_authors"})
    public String listAuthors() {
        List<Author> authors = authorDao.getAll();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME");
        at.addRule();
        authors.forEach(author -> {
            at.addRow(author.getId(), author.getName());
            at.addRule();
        });
        return at.render();
    }

    @ShellMethod(value = "List genres", key = {"lg", "list_genres"})
    public String listGenres() {
        List<Genre> genres = genreDao.getAll();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME");
        at.addRule();
        genres.forEach(genre -> {
            at.addRow(genre.getId(), genre.getName());
            at.addRule();
        });
        return at.render();
    }

    @ShellMethod(value = "Add genre", key = {"ig", "insert_genre"})
    public String insertGenre(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        try{
            genreDao.insert(new Genre(id, name));
            return "Success";
        } catch (DuplicateKeyException e) {
            return "Genre already exists";
        }
    }

    @ShellMethod(value = "Add author", key = {"ia", "insert_author"})
    public String insertAuthor(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        try{
            authorDao.insert(new Author(id, name));
            return "Success";
        } catch (DuplicateKeyException e) {
            return "Author already exists";
        }
    }

    @ShellMethod(value = "Add book", key = {"ib", "insert_book"})
    public String insertBook(@ShellOption("--id") Long id, @ShellOption("--title") String title, @ShellOption("--genre") Long genre, @ShellOption("--author") Long author) {

        System.out.println(id + " " + title + " " + genre + " " + author);
        Genre genreDaoById = genreDao.getById(genre);
        if(genreDaoById == null) {
            return "Genre not found";
        }
        Author authorDaoById = authorDao.getById(author);
        if(authorDaoById == null) {
            return "Author not found";
        }
        try{
            bookDao.insert(new Book(id, title, genreDaoById, authorDaoById));
            return "Success";
        } catch (DuplicateKeyException e) {
            return "Book already exists";
        }
    }

    @ShellMethod(value = "Update genre", key = {"ug", "update_genre"})
    public String updateGenre(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        if(genreDao.getById(id) == null) {
            return "Genre not found";
        }
        genreDao.update(new Genre(id, name));
        return "Genre updated";
    }

    @ShellMethod(value = "Update author", key = {"ua", "update_author"})
    public String updateAuthor(@ShellOption("--id") Long id, @ShellOption("--name") String name) {
        if(authorDao.getById(id) == null) {
            return "Author not found";
        }
        authorDao.update(new Author(id, name));
        return "Author updated";
    }

    @ShellMethod(value = "Update book", key = {"ub", "update_book"})
    public String updateBook(@ShellOption("--id") Long id, @ShellOption("--name") String title, @ShellOption("--genre") Long genre, @ShellOption("--author") Long author) {
        if(bookDao.getById(id) == null) {
            return "Book not found";
        }
        Genre genreDaoById = genreDao.getById(genre);
        if(genreDaoById == null) {
            return "Genre not found";
        }
        Author authorDaoById = authorDao.getById(author);
        if(authorDaoById == null) {
            return "Author not found";
        }
        bookDao.update(new Book(id, title, genreDaoById, authorDaoById));
        return "Success";
    }

    @ShellMethod(value = "Get genre", key = {"gg", "get_genre"})
    public String getGenre(@ShellOption("--id") Long id) {
        Genre genre = genreDao.getById(id);
        if(genre == null){
            return "Genre not found";
        } else {
            return genre.toString();
        }
    }

    @ShellMethod(value = "Get author", key = {"ga", "get_author"})
    public String getAuthor(@ShellOption("--id") Long id) {
        Author author = authorDao.getById(id);
        if(author == null) {
            return "Author not found";
        } else {
            return author.toString();
        }
    }

    @ShellMethod(value = "Get book", key = {"gb", "get_book"})
    public String getBook(@ShellOption("--id") Long id) {
        Book book = bookDao.getById(id);
        if(book == null) {
            return "Book not found";
        } else {
            return book.toString();
        }
    }

    @ShellMethod(value = "Delete book", key = {"db", "delete_book"})
    public String deleteBook(@ShellOption("--id") Long id) {
        int result = bookDao.deleteById(id);
        if(result == 1) {
            return "Book deleted";
        } else {
            if(bookDao.getById(id) == null) {
                return "Book not found";
            }
        }
        return "Can't delete";
    }

    @ShellMethod(value = "Delete author", key = {"da", "delete_author"})
    public String deleteAuthor(@ShellOption("--id") Long id) {

        if(bookDao.getCountAuthorById(id) > 0){
            return "You can't delete an author because books with him exist";
        }

        int result = authorDao.deleteById(id);
        if(result == 1) {
            return "Author deleted";
        } else {
            if(authorDao.getById(id) == null) {
                return "Author not found";
            }
        }
        return "Can't delete";
    }

    @ShellMethod(value = "Delete genre", key = {"dg", "delete_genre"})
    public String deleteGenre(@ShellOption("--id") Long id) {

        if(bookDao.getCountGenreById(id) > 0){
            return "You can't delete an genre because books with it exist";
        }

        int result = genreDao.deleteById(id);
        if(result == 1) {
            return "Genre deleted";
        } else {
            if(genreDao.getById(id) == null) {
                return "Genre not found";
            }
        }
        return "Can't delete";
    }

}
