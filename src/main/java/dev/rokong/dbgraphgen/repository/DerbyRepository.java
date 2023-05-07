package dev.rokong.dbgraphgen.repository;

import dev.rokong.dbgraphgen.entity.DbObj;
import dev.rokong.dbgraphgen.entity.Dependency;

import java.util.ArrayList;
import java.util.List;

public interface DerbyRepository {
    List<DbObj> findAllDbObj();
    List<Dependency> findAllDependency();
    StringBuffer findSource(DbObj proc);
    String getUsername();
}