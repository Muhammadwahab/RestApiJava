package com.example.restapilearning.database;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    // Constructors, getters, and setters...

    public Roles() {
    }

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Roles(String roleName) {
        this.roleName = roleName;
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
