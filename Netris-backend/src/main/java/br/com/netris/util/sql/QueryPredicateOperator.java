package br.com.netris.util.sql;

public enum QueryPredicateOperator {
    EQUAL("eq","="),
    GREATER_THAN("gt",">"),
    GREATER_THAN_EQUAL("gte",">="),
    LESS_THAN("lt","<"),
    LESS_THAN_EQUAL("lte","<="),
    NOT_EQUAL("ne","<>"),
    LIKE("like","like"),
    ILIKE("ilike","ilike"),
    IN("in","in");

    private final String alias;
    private final String operator;

    QueryPredicateOperator(String alias, String operator){
        this.alias = alias;
        this.operator = operator;
    }

    public String getAlias() {
        return alias;
    }

    public String getOperator() {
        return operator;
    }

    public static QueryPredicateOperator getByAlias(String alias) {
        QueryPredicateOperator[] values = QueryPredicateOperator.values();
        for (QueryPredicateOperator value : values) {
            if (value.getAlias().equalsIgnoreCase(alias)) {
                return value;
            }
        }
        return null;
    }
}