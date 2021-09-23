package com.shf.jooq.listener;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2021/9/22 0:45
 */
@Slf4j
public class PrettyPrinterExecuteListener extends DefaultExecuteListener {

    /**
     * Hook into the query execution lifecycle before executing queries
     */
    @Override
    public void executeStart(ExecuteContext ctx) {

        // Create a new DSLContext for logging rendering purposes
        // This DSLContext doesn't need a connection, only the SQLDialect...
        DSLContext create = DSL.using(ctx.dialect(),

                // ... and the flag for pretty-printing
                new Settings().withRenderFormatted(true));

        // If we're executing a query
        if (ctx.query() != null) {
           log.info("Execute sql : \n{}",create.renderInlined(ctx.query()));
        }

        // If we're executing a routine
        else if (ctx.routine() != null) {
            log.info(create.renderInlined(ctx.routine()));
        }
    }
}