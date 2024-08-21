package de.tum.nihaoshijie.flightsystem.server.repository.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    @Query("select e from Instruction e " +
            "where e.type = :type")
    List<Instruction> getInstructionByType(@Param("type") String type);
}