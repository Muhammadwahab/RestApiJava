package com.example.restapilearning.database;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import java.util.List;

@Stateless
public class RoleService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Transactional
    public List<Roles> getAllRoles() {
        return entityManager.createQuery("SELECT r FROM Roles r", Roles.class).getResultList();
    }

    @Transactional
    public Roles getRoleById(Long roleId) {
        return entityManager.find(Roles.class, roleId);
    }

    @Transactional
    public Roles createRole(Roles roles) {
        entityManager.persist(roles);
        return roles;
    }



    @Transactional
    public Roles getOrCreateRole(String roleName) {
        Roles role = entityManager.createQuery("SELECT r FROM Roles r WHERE r.roleName = :roleName", Roles.class)
                .setParameter("roleName", roleName)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (role == null) {
            role = new Roles(roleName);
            role.setRoleName(roleName);
            createRole(role);
//            entityManager.persist(role);
        }

        return role;
    }



    @Transactional
    public Roles updateRoles(Roles roles) {

        try{
            entityManager.getTransaction().begin();
            roles=entityManager.merge(roles);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            entityManager.getTransaction().rollback();
            throw new InternalServerErrorException(e);
        }


        return roles;
    }
    @Transactional
    public Roles getRole(String roleName) {
        Roles role = entityManager.createQuery("SELECT r FROM Roles r WHERE r.roleName = :roleName", Roles.class)
                .setParameter("roleName", roleName)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        return role;
    }
}
