package com.shf.jooq;

import com.shf.jooq.listener.DeleteOrUpdateWithoutWhereException;
import com.shf.jooq.tables.Author;
import com.shf.jooq.tables.AuthorBook;
import com.shf.jooq.tables.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description :
 * Run with system var : -Dorg.jooq.no-logo=true
 *
 * @author songhaifeng
 * @date 2021/9/18 14:04
 */
@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
@Slf4j
public class SampleTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert() {
        Author author = Author.AUTHOR;
        Book book = Book.BOOK;
        AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;

        dslContext.insertInto(author)
                .set(author.ID, 4)
                .set(author.FIRST_NAME, "Herbert")
                .set(author.LAST_NAME, "Schildt")
                .execute();
        dslContext.insertInto(book)
                .set(book.ID, 4)
                .set(book.TITLE, "A Beginner's Guide")
                .execute();
        dslContext.insertInto(authorBook)
                .set(authorBook.AUTHOR_ID, 4)
                .set(authorBook.BOOK_ID, 4)
                .execute();
    }

    @Test
    public void query() {
        Author author = Author.AUTHOR;
        Book book = Book.BOOK;
        AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;

        Result<Record2<String, Integer>> result = dslContext
                .select(author.LAST_NAME, DSL.count())
                .from(author)
                .leftJoin(authorBook)
                .on(author.ID.equal(authorBook.AUTHOR_ID))
                .join(book)
                .on(authorBook.BOOK_ID.equal(book.ID))
                .groupBy(author.LAST_NAME)
                .fetch();
        log.info("\n{}", result.format());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenInvalidData_whenInserting_thenFail() {
        AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;

        dslContext.insertInto(authorBook)
                .set(authorBook.AUTHOR_ID, 4)
                .set(authorBook.BOOK_ID, 5)
                .execute();
    }

    @Test
    @Rollback
    @Transactional
    public void update() {
        Author author = Author.AUTHOR;
        Book book = Book.BOOK;
        AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;

        dslContext.update(author)
                .set(author.LAST_NAME, "Baeldung")
                .where(author.ID.equal(3))
                .execute();
        dslContext.update(book)
                .set(book.TITLE, "Building your REST API with Spring")
                .where(book.ID.equal(3))
                .execute();
        dslContext.insertInto(authorBook)
                .set(authorBook.AUTHOR_ID, 3)
                .set(authorBook.BOOK_ID, 3)
                .execute();

        Result<Record3<Integer, String, String>> result = dslContext
                .select(author.ID, author.LAST_NAME, book.TITLE)
                .from(author)
                .join(authorBook)
                .on(author.ID.equal(authorBook.AUTHOR_ID))
                .join(book)
                .on(authorBook.BOOK_ID.equal(book.ID))
                .where(author.ID.equal(3))
                .fetch();

        log.info("\n{}", result.format());
    }

    @Test
    @Rollback
    @Transactional
    public void delete() {
        Author author = Author.AUTHOR;
        dslContext.delete(author)
                .where(author.ID.lt(3))
                .execute();

        Result<Record3<Integer, String, String>> result = dslContext
                .select(author.ID, author.FIRST_NAME, author.LAST_NAME)
                .from(author)
                .fetch();
        log.info(result.format());
    }

    @Test(expected = DeleteOrUpdateWithoutWhereException.class)
    @Rollback
    @Transactional
    public void delete_without_where() {
        Author author = Author.AUTHOR;
        dslContext.delete(author)
                .execute();
    }

    /**
     * 通过{@link JdbcTemplate}执行则无法触发ExecuteListener，jooq仅仅充当了sql构造器。
     * https://www.jooq.org/doc/latest/manual/sql-execution/alternative-execution-models/using-jooq-with-jdbctemplate/
     */
    @Test
    public void jdbcTemplateWithJooq() {
        Author author = Author.AUTHOR;
        Book book = Book.BOOK;
        AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;

        ResultQuery<Record2<String, Integer>> query = dslContext
                .select(author.LAST_NAME, DSL.count())
                .from(author)
                .leftJoin(authorBook)
                .on(author.ID.equal(authorBook.AUTHOR_ID))
                .join(book)
                .on(authorBook.BOOK_ID.equal(book.ID))
                .groupBy(author.LAST_NAME);

        List<AggregationResult> result = jdbcTemplate.query(
                query.getSQL(),
                (r, i) -> new AggregationResult(
                        r.getString(1),
                        r.getInt(2)
                ),
                query.getBindValues().toArray()
        );
        log.info("{}", result);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class AggregationResult {
        private String key;
        private int count;
    }

    @Autowired
    private Configuration configuration;

    /**
     * 通过{@link DSLContext#parser()}方法可以将原生sql转换为jooq的query对象，然后查询。
     * {@link DSLContext#parser()}提供了大量的转换能力
     * https://www.jooq.org/doc/latest/manual/sql-building/sql-parser/sql-parser-listener/
     */
    @Test
    public void parseSQL() {
        String sql = "select last_name, count(*) icount from author t1 left outer join author_book on t1.id = author_id join book t2 on book_id = t2.id group by last_name";
        Select query =
                DSL.using(configuration)
                        .parser()
                        .parseSelect(sql);

        Result<Record2<String, Integer>> result = dslContext.selectQuery(query).fetch();
        log.info("\n{}", result.format());

        // https://www.jooq.org/doc/latest/manual/sql-building/bind-values/sql-injection/
        Result<Record>  nativeResult = dslContext.fetch(sql);
        log.info("\n{}", nativeResult);
    }
}
