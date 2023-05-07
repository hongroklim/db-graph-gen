package dev.rokong.dbgraphgen.entity;

import jakarta.persistence.*;

@Entity
public class Dependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="obj_id")
    private DbObj obj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ref_id")
    private DbObj ref;

    public Dependency(DbObj obj, DbObj ref) {
        this.obj = obj;
        this.ref = ref;
    }

    public Dependency() {
    }

    public Long getId() {
        return id;
    }

    public DbObj getObj() {
        return obj;
    }

    public DbObj getRef() {
        return ref;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "id=" + id +
                ", objId=" + obj.getId() +
                ", refId=" + ref.getId() +
                '}';
    }
}
