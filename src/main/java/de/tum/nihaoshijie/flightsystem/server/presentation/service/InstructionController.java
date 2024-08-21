package de.tum.nihaoshijie.flightsystem.server.presentation.service;

import de.tum.nihaoshijie.flightsystem.server.business.service.InstructionService;
import de.tum.nihaoshijie.flightsystem.server.persistence.InFlightService;
import de.tum.nihaoshijie.flightsystem.server.persistence.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InstructionController {
    private InstructionService instructionService;

    @Autowired
    public InstructionController(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    //This endpoint fetches instruction by type
    @GetMapping( "/instruction")
    @ResponseBody
    public ResponseEntity<List<Instruction>> getInstruction(@RequestParam("type") String type) {
        return ResponseEntity.ok(instructionService.getInstructionByType(type));
    }

    //This endpoint fetches all instructions
    @GetMapping( "/instruction/all")
    @ResponseBody
    public ResponseEntity<List<Instruction>> getAllInstruction() {
        return ResponseEntity.ok(instructionService.getAllInstruction());
    }

    //This endpoint creates or updates an instruction, not for client
    @PostMapping("/instruction")
    @ResponseBody
    public void saveInstruction(@RequestBody Instruction instruction) {
        instructionService.addInstruction(instruction);
    }
}
