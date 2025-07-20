package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.GetInsurancePolicyByIdUseCase;
import com.felipe.policy.event.processor.application.usecases.ListPoliciesByCustomerUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {

    private final CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    private final GetInsurancePolicyByIdUseCase getInsurancePolicyByIdUseCase;

    private final ListPoliciesByCustomerUseCase listPoliciesByCustomerUseCase;

    private final CancelInsurancePolicyUseCase cancelInsurancePolicyUseCase;

    @PostMapping
    public ResponseEntity<InsurancePolicyResponseDTO> create(@Valid @RequestBody InsurancePolicyRequestDTO dto) {
        InsurancePolicyResponseDTO response = createInsurancePolicyUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponseDTO> getById(@PathVariable UUID id) {
        InsurancePolicyResponseDTO response = getInsurancePolicyByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InsurancePolicyResponseDTO>> getByCustomer(@PathVariable UUID customerId) {
        List<InsurancePolicyResponseDTO> response = listPoliciesByCustomerUseCase.execute(customerId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPolicy(@PathVariable UUID id) {
        cancelInsurancePolicyUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}