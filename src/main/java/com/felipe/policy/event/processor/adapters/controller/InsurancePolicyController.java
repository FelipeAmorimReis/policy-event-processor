package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.GetInsurancePolicyByIdUseCase;
import com.felipe.policy.event.processor.application.usecases.ReprocessInsurancePolicyUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {

    private final CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    private final GetInsurancePolicyByIdUseCase getInsurancePolicyByIdUseCase;

    private final CancelInsurancePolicyUseCase cancelInsurancePolicyUseCase;

    private final ReprocessInsurancePolicyUseCase reprocessInsurancePolicyUseCase;

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

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelPolicyResponseDTO> cancel(@PathVariable UUID id) {
        CancelPolicyResponseDTO response = cancelInsurancePolicyUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reprocess")
    public ResponseEntity<ReprocessPolicyResponseDTO> reprocess(@PathVariable UUID id) {
        ReprocessPolicyResponseDTO response = reprocessInsurancePolicyUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}