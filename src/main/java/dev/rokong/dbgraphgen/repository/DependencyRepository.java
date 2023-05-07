package dev.rokong.dbgraphgen.repository;

import dev.rokong.dbgraphgen.entity.Dependency;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DependencyRepository extends JpaRepository<Dependency, Long> {
    @Override
    @EntityGraph(attributePaths = {"obj", "ref"})
    List<Dependency> findAll();

    @Override
    @EntityGraph(attributePaths = {"obj", "ref"})
    Optional<Dependency> findById(Long id);

    @Query("DELETE FROM Dependency WHERE obj IN (SELECT id FROM DbObj WHERE owner = :owner)")
    void deleteByObjOwner(@Param("owner") String owner);
}
