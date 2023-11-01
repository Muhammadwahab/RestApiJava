package com.example.restapilearning.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import java.util.List;


public class UserService {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");


    private EntityManager entityManager=emf.createEntityManager();

//    @PersistenceContext(unitName = "default",name = "default")
//     EntityManager entityManager;



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
    public List<User> getAllUser() {

        return entityManager.createQuery("SELECT u FROM users u", User.class).getResultList();
    }

    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

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
