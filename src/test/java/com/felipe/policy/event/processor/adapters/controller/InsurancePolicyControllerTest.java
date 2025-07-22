package com.felipe.policy.event.processor.adapters.controller;

import com.felipe.policy.event.processor.application.dto.request.InsurancePolicyRequestDTO;
import com.felipe.policy.event.processor.application.dto.response.CancelPolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.InsurancePolicyResponseDTO;
import com.felipe.policy.event.processor.application.dto.response.ReprocessPolicyResponseDTO;
import com.felipe.policy.event.processor.application.usecases.CancelInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.CreateInsurancePolicyUseCase;
import com.felipe.policy.event.processor.application.usecases.GetInsurancePolicyByIdUseCase;
import com.felipe.policy.event.processor.application.usecases.ReprocessInsurancePolicyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InsurancePolicyControllerTest {

    private final CreateInsurancePolicyUseCase createUseCase = mock(CreateInsurancePolicyUseCase.class);
    private final GetInsurancePolicyByIdUseCase getByIdUseCase = mock(GetInsurancePolicyByIdUseCase.class);
    private final CancelInsurancePolicyUseCase cancelUseCase = mock(CancelInsurancePolicyUseCase.class);
    private final ReprocessInsurancePolicyUseCase reprocessUseCase = mock(ReprocessInsurancePolicyUseCase.class);

    private final InsurancePolicyController controller = new InsurancePolicyController(
            createUseCase, getByIdUseCase, cancelUseCase, reprocessUseCase
    );

    @Test
    @DisplayName("Create insurance policy successfully")
    void createInsurancePolicySuccessfully() {
        InsurancePolicyRequestDTO requestDTO = new InsurancePolicyRequestDTO();
        InsurancePolicyResponseDTO responseDTO = new InsurancePolicyResponseDTO();
        when(createUseCase.execute(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<InsurancePolicyResponseDTO> response = controller.create(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(createUseCase, times(1)).execute(requestDTO);
    }

    @Test
    @DisplayName("Get insurance policy by ID successfully")
    void getInsurancePolicyByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        InsurancePolicyResponseDTO responseDTO = new InsurancePolicyResponseDTO();
        when(getByIdUseCase.execute(id)).thenReturn(responseDTO);

        ResponseEntity<InsurancePolicyResponseDTO> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(getByIdUseCase, times(1)).execute(id);
    }

    @Test
    @DisplayName("Cancel insurance policy successfully")
    void cancelInsurancePolicySuccessfully() {
        UUID id = UUID.randomUUID();
        CancelPolicyResponseDTO responseDTO = new CancelPolicyResponseDTO();
        when(cancelUseCase.execute(id)).thenReturn(responseDTO);

        ResponseEntity<CancelPolicyResponseDTO> response = controller.cancel(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(cancelUseCase, times(1)).execute(id);
    }

    @Test
    @DisplayName("Reprocess insurance policy successfully")
    void reprocessInsurancePolicySuccessfully() {
        UUID id = UUID.randomUUID();
        ReprocessPolicyResponseDTO responseDTO = new ReprocessPolicyResponseDTO();
        when(reprocessUseCase.execute(id)).thenReturn(responseDTO);

        ResponseEntity<ReprocessPolicyResponseDTO> response = controller.reprocess(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(reprocessUseCase, times(1)).execute(id);
    }

    @Test
    @DisplayName("Get insurance policy by ID not found")
    void getInsurancePolicyByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(getByIdUseCase.execute(id)).thenThrow(new RuntimeException("Policy not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getById(id));

        assertEquals("Policy not found", exception.getMessage());
        verify(getByIdUseCase, times(1)).execute(id);
    }
}