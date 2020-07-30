package br.com.netris.service;

import br.com.netris.dao.ResourceDAO;
import br.com.netris.exception.ServiceException;
import br.com.netris.util.sql.SqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class ResourceService {

    @Autowired
    private ResourceDAO resourceDAO;

    public List findAll(String module, String entity, HashMap<String, String> params) throws ClassNotFoundException {
        SqlBuilder sqlBuilder = new SqlBuilder(params);

        String sql = sqlBuilder.toSql(getTableName(module, entity));

        return resourceDAO.findBySQL(sql);
    }

    private String getTableName(String module, String entity) throws ClassNotFoundException {
        Class<?> entityClass = Class.forName("br.com.netris.entity." + module + "." + entity);
        String tableName = entityClass.getAnnotation(Table.class).name();
        String schemaName = entityClass.getAnnotation(Table.class).schema();
        return schemaName+"."+tableName;
    }

}