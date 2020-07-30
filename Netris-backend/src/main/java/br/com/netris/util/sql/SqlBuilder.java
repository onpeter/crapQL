package br.com.netris.util.sql;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import java.util.*;

public class SqlBuilder {

    public static final int LIMIT_MAX = 100;
    public static final int LIMIT_DEFAULT = 10;
    public static final int PAGE_DEFAULT = 1;

    private boolean distinct;
    private int limit;
    private int page;
    private List<String> projection;
    private List<QueryPredicate> predicate;
    private List<QuerySort> sort;

    public SqlBuilder(HashMap<String, String> params) {
        buildSql(params);
    }

    private void buildSql(HashMap<String, String> params) {

        resetAtributes();

        setDistinct(params);

        setLimit(params);

        setPage(params);

        setProjection(params);

        setSort(params);

        HashMap<String,String> mapWithOnlyPredicate = createPredicateMap(params);

        setPredicate(mapWithOnlyPredicate);
    }

    private void resetAtributes() {
        this.distinct = false;
        this.limit = LIMIT_DEFAULT;
        this.page = PAGE_DEFAULT;
        this.projection = new ArrayList<>();
        this.predicate = new ArrayList<>();
        this.sort = new ArrayList<>();
    }

    private HashMap<String,String> createPredicateMap(HashMap<String, String> params) {
        HashMap<String,String> mapWithOnlyPredicate = new HashMap<>(params);
        mapWithOnlyPredicate.remove("distinct");
        mapWithOnlyPredicate.remove("limit");
        mapWithOnlyPredicate.remove("page");
        mapWithOnlyPredicate.remove("projection");
        mapWithOnlyPredicate.remove("sort");
        return mapWithOnlyPredicate;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    private void setDistinct(Map<String, String> params) {
        if (params.containsKey("distinct") && params.get("distinct").equalsIgnoreCase("true")) {
            this.distinct = true;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private void setLimit(Map<String, String> params) {
        if (params.containsKey("limit")) {
            int limit = Integer.valueOf(params.get("limit")).intValue();

            if (limit > LIMIT_MAX)
                this.limit = LIMIT_MAX;
            else
                this.limit = limit;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private void setPage(Map<String, String> params) {
        if (params.containsKey("page")) {
            this.page = Integer.valueOf(params.get("page")).intValue();
        }
    }

    public int getOffset() {
        return (this.page - 1) * this.limit;
    }

    private void setProjection(Map<String, String> params) {
        if (params.containsKey("projection")) {
            String projection = params.get("projection");
            this.projection = Arrays.asList(projection.split(","));
        }
    }

    private void setSort(Map<String, String> params) {
        if (params.containsKey("sort")) {
            String sorts = params.get("sort");
            List<String> listSort = Arrays.asList(sorts.split(","));
            for (String sort : listSort) {
                QuerySortDirection direction = QuerySortDirection.ASC;
                String column;
                if (sort.startsWith("desc:")) {
                    direction = QuerySortDirection.DESC;
                    column = sort.replace("desc:","");
                } else {
                    column  = sort.replace("asc:","");
                }
                this.sort.add(new QuerySort(direction,column));
            }
        }
    }

    public List<QueryPredicate> getPredicate() {
        return predicate;
    }

    public void setPredicate(List<QueryPredicate> predicate) {
        this.predicate = predicate;
    }

    private void setPredicate(Map<String, String> params) {
        for (Map.Entry<String, String> predicate : params.entrySet()) {
            String column = predicate.getKey();
            String value = predicate.getValue();
            String[] conditions = value.split("\\|");
            for (String condition : conditions) {
                List<String> values = Arrays.asList(condition.split(":"));
                QueryPredicateOperator operator = QueryPredicateOperator.getByAlias(values.get(0));
                if (operator == null) {
                    value = condition;
                    operator = QueryPredicateOperator.EQUAL;
                } else {
                    value = StringUtil.join(values.subList(1,values.size()),":");
                }

                this.predicate.add(new QueryPredicate(column, operator, value));
            }
        }
    }

    @Override
    public String toString() {
        String projection = this.projection.toString();

        List<String> predicate = new ArrayList<>();
        for (QueryPredicate queryPredicate : this.predicate) {
            predicate.add(queryPredicate.toString());
        }

        List<String> sort = new ArrayList<>();
        for (QuerySort querySort : this.sort) {
            sort.add(querySort.toString());
        }

        StringBuilder retorno = new StringBuilder();
        return retorno
                .append("Projection: ")
                .append(projection)
                .append(" Predicate: ")
                .append(predicate)
                .append(" Sort: ")
                .append(sort)
                .append(" Distinct: ")
                .append(this.distinct)
                .append(" Limit: ")
                .append(this.limit)
                .append(" Page: ")
                .append(this.page)
                .toString();
    }

    public String toSql(String tableName) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");

        if (this.distinct)
            sql.append("DISTINCT ");

        if (this.projection.isEmpty())
            sql.append("*");
        else
            sql.append(StringUtils.join(this.projection, ", "));

        sql.append(" FROM ").append(tableName);

        if (!this.predicate.isEmpty()) {
            sql.append(" WHERE ");
            List<String> listPredicate = new ArrayList<>();
            for (QueryPredicate queryPredicate : this.predicate) {
                listPredicate.add(queryPredicate.toSql());
            }
            sql.append(StringUtils.join(listPredicate, " AND "));
        }

        if (!this.sort.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> listSort = new ArrayList<>();
            for (QuerySort querySort : this.sort) {
                listSort.add(querySort.toSql());
            }
            sql.append(StringUtils.join(listSort, ", "));
        }

        sql.append(" LIMIT ").append(limit);

        sql.append(" OFFSET ").append(getOffset());

        return sql.toString();
    }
}
