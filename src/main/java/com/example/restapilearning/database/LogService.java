package com.example.restapilearning.database;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;


@Stateless
public class LogService {


    @PersistenceContext(unitName = "default")
    EntityManager entityManager;


    @Transactional
    public ApiRequest addLogged(ApiRequest apiRequest) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(apiRequest);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error");
        }
        return apiRequest;
    }


}
