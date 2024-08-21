package de.tum.nihaoshijie.flightsystem.server.business.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.Instruction;
import de.tum.nihaoshijie.flightsystem.server.repository.service.InstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructionService {

    private InstructionRepository instructionRepository;

    @Autowired
    public InstructionService (InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    public void addInstruction(Instruction instruction) {
        instructionRepository.save(instruction);
    }

    public List<Instruction> getInstructionByType (String type) {
        return instructionRepository.getInstructionByType(type);
    }


    public List<Instruction> getAllInstruction () {
        return instructionRepository.findAll();
    }
}
