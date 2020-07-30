package br.com.netris.dao;

import org.hibernate.ejb.HibernateQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class ResourceDAO{

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional(readOnly = true)
    public List findBySQL(String sql) throws ClassNotFoundException {
        Query query = entityManager.createNativeQuery(sql);

        org.hibernate.Query hibernateQuery = ((HibernateQuery)query).getHibernateQuery();
        hibernateQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String,Object>> list = hibernateQuery.list();

        return list;
    }

}