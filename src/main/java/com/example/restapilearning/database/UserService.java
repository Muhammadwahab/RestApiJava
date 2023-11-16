package com.example.restapilearning.database;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import java.util.List;


@Stateless
public class UserService {

//    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");


//    private EntityManager entityManager=emf.createEntityManager();

    @PersistenceContext(unitName = "default")
     EntityManager entityManager;



    @Transactional
    public User addUser(User user) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }catch (Exception e)
        {
            throw new InternalServerErrorException("Internal Server Error");
        }
        return user;
    }


    @Transactional
    public List<User> getAllUser(int page, int pageSize) {
        int start = (page - 1) * pageSize;

        return entityManager.createQuery("SELECT u FROM users u", User.class).setFirstResult(start).setMaxResults(pageSize).getResultList();
    }

    public long getItemCount() {
        return entityManager.createQuery("SELECT COUNT(u) FROM users u", Long.class)
                .getSingleResult();
    }

    @Transactional
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

   @Transactional
    public User updateUser(User user) {

        try{
            entityManager.getTransaction().begin();
             user=entityManager.merge(user);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            entityManager.getTransaction().rollback();
            throw new InternalServerErrorException(e);
        }


        return user;
    }

    public void deleteUser(Long id) {
        User book = entityManager.find(User.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
