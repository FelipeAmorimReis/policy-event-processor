package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {
    private final CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    @PostMapping
    public ResponseEntity<InsurancePolicyResponseDTO> create(@RequestBody InsurancePolicyRequestDTO dto) {
        InsurancePolicyResponseDTO response = createInsurancePolicyUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}