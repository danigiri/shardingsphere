/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.binder.segment.expression.type;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.binder.segment.SegmentType;
import org.apache.shardingsphere.infra.binder.segment.expression.ExpressionSegmentBinder;
import org.apache.shardingsphere.infra.binder.segment.from.context.TableSegmentBinderContext;
import org.apache.shardingsphere.infra.binder.statement.SQLStatementBinderContext;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.statement.core.segment.dml.expr.NotExpression;

import java.util.Collections;
import java.util.Map;

/**
 * Not expression binder.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotExpressionBinder {
    
    /**
     * Bind not expression segment.
     *
     * @param segment not expression
     * @param parentSegmentType parent segment type
     * @param binderContext SQL statement binder context
     * @param tableBinderContexts table binder contexts
     * @return bound not expression
     */
    public static NotExpression bind(final NotExpression segment, final SegmentType parentSegmentType,
                                     final SQLStatementBinderContext binderContext, final Map<String, TableSegmentBinderContext> tableBinderContexts) {
        ExpressionSegment boundExpression = ExpressionSegmentBinder.bind(segment.getExpression(), parentSegmentType, binderContext, tableBinderContexts, Collections.emptyMap());
        return new NotExpression(segment.getStartIndex(), segment.getStopIndex(), boundExpression, segment.getNotSign());
    }
}