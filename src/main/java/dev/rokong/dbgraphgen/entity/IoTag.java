package dev.rokong.dbgraphgen.entity;

import jakarta.persistence.*;

@Entity
public class IoTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="proc_id")
    private DbObj proc;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tab_id")
    private DbObj tab;
    private boolean isIn;
    private boolean isOut;

    public IoTag(DbObj proc, DbObj tab, boolean isIn, boolean isOut) {
        this.proc = proc;
        this.tab = tab;
        this.isIn = isIn;
        this.isOut = isOut;
    }

    public IoTag() {
    }

    public Long getId() {
        return id;
    }

    public DbObj getProc() {
        return proc;
    }

    public DbObj getTab() {
        return tab;
    }

    public boolean isIn() {
        return isIn;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    @Override
    public String toString() {
        return "IoTag{" +
                "id=" + id +
                ", procId=" + proc.getId() +
                ", tabId=" + tab.getId() +
                ", isIn=" + isIn +
                ", isOut=" + isOut +
                '}';
    }
}
