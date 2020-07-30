package br.com.netris.util;

import br.com.netris.util.sql.SqlBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlBuilderTest {

    @Test
    public void shouldCreateProjectionFromParams() {
        HashMap<String,String> params = new HashMap<>();

        params.put("projection","id,name,date");

        SqlBuilder sqlBuilder = new SqlBuilder(params);

        String expected = "Projection: [id, name, date] Predicate: [] Sort: [] Distinct: false Limit: 10 Page: 1";

        assertEquals(expected, sqlBuilder.toString());
    }

    @Test
    public void shouldCreateSortFromParams() {
        HashMap<String,String> params = new HashMap<>();

        params.put("sort","asc:id,desc:name,date");

        SqlBuilder sqlBuilder = new SqlBuilder(params);

        String expected = "Projection: [] Predicate: [] Sort: [id ASC, name DESC, date ASC] Distinct: false Limit: 10 Page: 1";

        assertEquals(expected, sqlBuilder.toString());
    }

    @Test
    public void shouldCreatePredicateFromParams() {
        HashMap<String,String> params = new HashMap<>();

        params.put("id","1");
        params.put("name","eq:Carlos");
        params.put("age","gt:18");
        params.put("height","lte:1.75");
        params.put("study_id","eq:166.65556.6552.02020415");
        params.put("description","altura:1.75 nome:Carlos age:18");
        params.put("last_name","like:Med:Bol%");

        SqlBuilder sqlBuilder = new SqlBuilder(params);

        String expected = "Projection: [] " +
                "Predicate: [" +
                "name = Carlos, " +
                "description = altura:1.75 nome:Carlos age:18, " +
                "last_name like Med:Bol%, " +
                "id = 1, " +
                "study_id = 166.65556.6552.02020415, " +
                "age > 18, " +
                "height <= 1.75] " +
                "Sort: [] Distinct: false Limit: 10 Page: 1";

        assertEquals(expected, sqlBuilder.toString());
    }

    @Test
    public void shouldCreateValidSql() {
        HashMap<String,String> params = new HashMap<>();

        params.put("projection","nome, pat_id, id_empresa");

        params.put("nome","Ilike:CARLOS%");
        params.put("id_empresa","in:1,2,3");

        params.put("sort","asc:nome");

        params.put("limit","1");
        params.put("page","5");

        SqlBuilder sqlBuilder = new SqlBuilder(params);

        String expected = "SELECT nome,  pat_id,  id_empresa " +
                "FROM netris.paciente WHERE nome ilike 'CARLOS%' AND id_empresa in ('1', '2', '3') " +
                "ORDER BY nome ASC LIMIT 1 OFFSET 4";

        assertEquals(expected, sqlBuilder.toSql("netris.paciente"));
    }
}