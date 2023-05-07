package dev.rokong.dbgraphgen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DbObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String owner;
    private String name;

    public DbObj(String type, String owner, String name) {
        this.type = type;
        this.owner = owner;
        this.name = name;
    }

    public DbObj() {
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DbObj{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
