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

package org.apache.shardingsphere.sharding.route.engine.validator.ddl.impl;

import org.apache.shardingsphere.infra.binder.context.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.statement.ddl.CreateIndexStatementContext;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.database.core.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.infra.hint.HintValueContext;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereSchema;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.sharding.exception.metadata.DuplicateIndexException;
import org.apache.shardingsphere.sharding.route.engine.validator.ddl.ShardingDDLStatementValidator;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sql.parser.statement.core.statement.ddl.CreateIndexStatement;

import java.util.Collections;
import java.util.List;

/**
 * Sharding create index statement validator.
 */
public final class ShardingCreateIndexStatementValidator extends ShardingDDLStatementValidator {
    
    @Override
    public void preValidate(final ShardingRule shardingRule, final SQLStatementContext sqlStatementContext, final HintValueContext hintValueContext,
                            final List<Object> params, final ShardingSphereDatabase database, final ConfigurationProperties props) {
        CreateIndexStatementContext createIndexStatementContext = (CreateIndexStatementContext) sqlStatementContext;
        CreateIndexStatement createIndexStatement = createIndexStatementContext.getSqlStatement();
        if (createIndexStatement.isIfNotExists()) {
            return;
        }
        String defaultSchemaName = new DatabaseTypeRegistry(sqlStatementContext.getDatabaseType()).getDefaultSchemaName(database.getName());
        ShardingSphereSchema schema = createIndexStatementContext.getTablesContext().getSchemaName()
                .map(database::getSchema).orElseGet(() -> database.getSchema(defaultSchemaName));
        validateTableExist(schema, Collections.singleton(createIndexStatement.getTable()));
        String tableName = createIndexStatement.getTable().getTableName().getIdentifier().getValue();
        String indexName = createIndexStatementContext.getIndexes().stream().map(each -> each.getIndexName().getIdentifier().getValue()).findFirst().orElse(null);
        ShardingSpherePreconditions.checkState(!schema.containsIndex(tableName, indexName), () -> new DuplicateIndexException(indexName));
    }
    
    @Override
    public void postValidate(final ShardingRule shardingRule, final SQLStatementContext sqlStatementContext, final HintValueContext hintValueContext, final List<Object> params,
                             final ShardingSphereDatabase database, final ConfigurationProperties props, final RouteContext routeContext) {
    }
}
