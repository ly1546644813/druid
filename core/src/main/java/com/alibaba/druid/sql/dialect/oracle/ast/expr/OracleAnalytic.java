/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class OracleAnalytic extends SQLOver implements SQLReplaceable, OracleExpr {
    private OracleAnalyticWindowing windowing;

    public OracleAnalytic() {
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }
        if (visitor.visit(this)) {
            acceptChild(visitor, this.partitionBy);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.windowing);
        }
        visitor.endVisit(this);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.partitionBy);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.windowing);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.partitionBy);
        if (this.orderBy != null) {
            children.add(orderBy);
        }
        if (this.windowing != null) {
            children.add(windowing);
        }
        return children;
    }

    public OracleAnalyticWindowing getWindowing() {
        return this.windowing;
    }

    public OracleAnalytic clone() {
        OracleAnalytic x = new OracleAnalytic();

        cloneTo(x);

        if (windowing != null) {
            x.setWindowing(windowing.clone());
        }

        return x;
    }

    public void setWindowing(OracleAnalyticWindowing x) {
        if (x != null) {
            x.setParent(this);
        }
        this.windowing = x;
    }

    public SQLDataType computeDataType() {
        return null;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        for (int i = 0; i < partitionBy.size(); i++) {
            if (partitionBy.get(i) == expr) {
                partitionBy.set(i, target);
                return true;
            }
        }

        return false;
    }
}
