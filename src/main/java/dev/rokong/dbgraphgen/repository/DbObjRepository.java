package dev.rokong.dbgraphgen.repository;

import dev.rokong.dbgraphgen.entity.DbObj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbObjRepository extends JpaRepository<DbObj, Long> {
    List<DbObj> findByType(String type);
    List<DbObj> findByTypeAndOwner(String type, String owner);
    void deleteByOwner(String owner);
}
