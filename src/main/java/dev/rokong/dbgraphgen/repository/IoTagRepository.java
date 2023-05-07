package dev.rokong.dbgraphgen.repository;

import dev.rokong.dbgraphgen.entity.DbObj;
import dev.rokong.dbgraphgen.entity.IoTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IoTagRepository extends JpaRepository<IoTag, Long> {
    @EntityGraph(attributePaths = {"tab"})
    List<IoTag> findByProc(DbObj proc);

    @Modifying @Transactional
    @Query("DELETE FROM IoTag WHERE proc IN (SELECT id FROM DbObj WHERE owner = :owner)")
    void deleteByProcOwner(@Param("owner") String owner);

    @Modifying @Transactional
    @Query("INSERT INTO IoTag (proc, tab) SELECT obj, ref FROM Dependency WHERE obj IN (SELECT id FROM DbObj WHERE owner = :owner)")
    void insertDependencyByProcOwner(@Param("owner") String owner);
}