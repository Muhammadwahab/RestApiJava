package com.example.restapilearning.database;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    // Additional methods for updating and deleting roles if needed
}
