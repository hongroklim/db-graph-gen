package dev.rokong.dbgraphgen.vo;

import dev.rokong.dbgraphgen.entity.DbObj;

public class IoTagVO {
    private String owner;
    private String name;
    boolean isIn;
    boolean isOut;

    public IoTagVO(String owner, String name, boolean isIn, boolean isOut) {
        this.owner = owner;
        this.name = name;
        this.isIn = isIn;
        this.isOut = isOut;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public boolean isIn() {
        return isIn;
    }

    public boolean isOut() {
        return isOut;
    }

}
