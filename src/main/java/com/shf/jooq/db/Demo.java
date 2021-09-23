/*
 * This file is generated by jOOQ.
 */
package com.shf.jooq.db;


import com.shf.jooq.tables.Author;
import com.shf.jooq.tables.AuthorBook;
import com.shf.jooq.tables.Book;
import com.shf.jooq.tables.Sample;
import com.shf.jooq.tables.User;
import com.shf.jooq.tables.Users;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Demo extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>demo</code>
     */
    public static final Demo DEMO = new Demo();

    /**
     * The table <code>demo.author</code>.
     */
    public final Author AUTHOR = Author.AUTHOR;

    /**
     * The table <code>demo.author_book</code>.
     */
    public final AuthorBook AUTHOR_BOOK = AuthorBook.AUTHOR_BOOK;

    /**
     * The table <code>demo.book</code>.
     */
    public final Book BOOK = Book.BOOK;

    /**
     * The table <code>demo.sample</code>.
     */
    public final Sample SAMPLE = Sample.SAMPLE;

    /**
     * The table <code>demo.user</code>.
     */
    public final User USER = User.USER;

    /**
     * The table <code>demo.users</code>.
     */
    public final Users USERS = Users.USERS;

    /**
     * No further instances allowed
     */
    private Demo() {
        super("demo", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Author.AUTHOR,
            AuthorBook.AUTHOR_BOOK,
            Book.BOOK,
            Sample.SAMPLE,
            User.USER,
            Users.USERS
        );
    }
}