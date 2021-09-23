package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.Param;

import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_SETTINGS;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2021/9/22 1:43
 */
public class PublicRenderContext extends DefaultRenderContext {

    private PublicRenderContext(Configuration configuration) {
        super(configuration);
    }

    public static PublicRenderContext render(Configuration c) {
        PublicRenderContext render = new PublicRenderContext(c);
        render.data(DATA_FORCE_SETTINGS, true);
        return render;
    }

    public class Rendered {
        String                  sql;
        QueryPartList<Param<?>> bindValues;
        int                     skipUpdateCounts;

        public Rendered(String sql, QueryPartList<Param<?>> bindValues, int skipUpdateCounts) {
            this.sql = sql;
            this.bindValues = bindValues;
            this.skipUpdateCounts = skipUpdateCounts;
        }

        @Override
        public String toString() {
            return sql;
        }
    }
}
