package com.example.restapilearning.database;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Singleton
@Startup
public class DatabaseSeeder {

    @EJB
    private RoleService roleService;

    @PostConstruct
    @Transactional
    public void seedDatabase() {
        // Add your seeding logic here

        // Example: Creating a user and a role
        roleService.getOrCreateRole("USER");
        roleService.getOrCreateRole("ADMIN");

    }
}
