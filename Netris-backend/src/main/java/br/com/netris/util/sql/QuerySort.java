package br.com.netris.util.sql;

public class QuerySort {

    private QuerySortDirection direction;
    private String column;

    public QuerySort(QuerySortDirection direction, String column) {
        this.direction = direction;
        this.column = column;
    }

    public QuerySortDirection getDirection() {
        return direction;
    }

    public void setDirection(QuerySortDirection direction) {
        this.direction = direction;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return this.column + " " + this.direction.toString();
    }

    public String toSql() {
        return this.column + " " + this.direction.toString();
    }
}
