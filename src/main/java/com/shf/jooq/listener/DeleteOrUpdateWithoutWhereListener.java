package com.shf.jooq.listener;

import lombok.extern.slf4j.Slf4j;
import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;

import java.util.Objects;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2021/9/22 0:37
 */
@Slf4j
public class DeleteOrUpdateWithoutWhereListener extends DefaultExecuteListener {

    @Override
    public void renderEnd(ExecuteContext ctx) {
        if (Objects.requireNonNull(ctx.sql()).matches("^(?i:(UPDATE|DELETE)(?!.* WHERE ).*)$")) {
            log.error("Update or delete SQL [{}] without where not supports.", ctx.sql());
            throw new DeleteOrUpdateWithoutWhereException();
        }
    }
}