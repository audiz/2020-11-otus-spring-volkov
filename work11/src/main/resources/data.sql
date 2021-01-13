insert into authors (id, `name`) values
    (1, 'George Orwell'),
    (2, 'Aldous Huxley');

insert into genres (id, `name`) values
    (1, 'Science Fiction');

insert into books (id, `title`, `genre`, `author`) values
    (1, 'Nineteen Eighty-Four', 1, 1),
    (2, 'Brave New World', 1, 2);

insert into comments (id, `comment`, `book_id`) values
    (1, 'Very well!!!', 1),
    (2, 'Not bad', 1);
