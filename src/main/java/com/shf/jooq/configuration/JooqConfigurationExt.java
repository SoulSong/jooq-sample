package com.shf.jooq.configuration;

import com.shf.jooq.listener.DeleteOrUpdateWithoutWhereListener;
import com.shf.jooq.listener.PrettyPrinterExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * description :
 * https://www.jooq.org/doc/latest/manual/sql-execution/execute-listeners/
 * {@link JooqAutoConfiguration}
 *
 * @author songhaifeng
 * @date 2021/9/22 0:37
 */
@Configuration
public class JooqConfigurationExt {

    @Bean
    @Order(1)
    public DefaultExecuteListenerProvider deleteOrUpdateWithoutWhereListenerProvider() {
        return new DefaultExecuteListenerProvider(new DeleteOrUpdateWithoutWhereListener());
    }

    @Bean
    @Order(-1)
    public DefaultExecuteListenerProvider prettyPrinterExecuteProvider() {
        return new DefaultExecuteListenerProvider(new PrettyPrinterExecuteListener());
    }
}
