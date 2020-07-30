package br.com.netris.util.sql;

import org.apache.commons.lang.StringUtils;

public class QueryPredicate {

    private String column;
    private QueryPredicateOperator operator;
    private String value;

    public QueryPredicate(String column, QueryPredicateOperator operator, String value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public QueryPredicateOperator getOperator() {
        return operator;
    }

    public void setOperator(QueryPredicateOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.column + " " + this.operator.getOperator() + " " + this.value;
    }

    public String toSql() {
        String value = this.value.replace("'", "\\'");
        if (this.operator.equals(QueryPredicateOperator.IN)) {
            return this.column + " " + this.operator.getOperator() + " ('" + StringUtils.join(value.split(","),"', '") + "')";
        }
        return this.column + " " + this.operator.getOperator() + " '" + value + "'";
    }
}
