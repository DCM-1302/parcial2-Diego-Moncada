package co.edu.unisabana.parcial.service;

import co.edu.unisabana.parcial.controller.dto.CheckpointDTO;
import co.edu.unisabana.parcial.service.model.Checkin;
import co.edu.unisabana.parcial.service.model.Checkout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckpointServiceTest {

    @Mock
    CheckpointDAO checkpointPort;

    @InjectMocks
    CheckpointService checkpointService;

    @Test
    void testCheckout_ValidData() {
        // Given
        CheckpointDTO checkpointDTO = new CheckpointDTO("facility1", "driver1", 15);
        Checkin lastCheckin = new Checkin("facility1", "driver1", 10);  // Último check-in encontrado

        Mockito.when(checkpointPort.findLastCheckin(checkpointDTO.driver, checkpointDTO.facility))
                .thenReturn(lastCheckin);

        // When
        checkpointService.checkout(checkpointDTO);

        // Then
        Mockito.verify(checkpointPort, times(1)).saveCheckout(any(Checkout.class));
    }

    @Test
    void testCheckout_NoPreviousCheckin_ThrowsException() {
        // Given
        CheckpointDTO checkpointDTO = new CheckpointDTO("facility1", "driver1", 15);

        Mockito.when(checkpointPort.findLastCheckin(checkpointDTO.driver, checkpointDTO.facility))
                .thenReturn(null);  // No se encontró un check-in previo

        // When / Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> checkpointService.checkout(checkpointDTO));
        assertEquals("don't exist previously check in", exception.getMessage());

        Mockito.verify(checkpointPort, never()).saveCheckout(any(Checkout.class));  // No debería llamar a saveCheckout
    }

    @Test
    void testCheckout_InvalidDayOfMonth_ThrowsException() {
        // Given
        CheckpointDTO checkpointDTO = new CheckpointDTO("facility1", "driver1", 32);
        Checkin lastCheckin = new Checkin("facility1", "driver1", 10);  // Último check-in encontrado

        Mockito.when(checkpointPort.findLastCheckin(checkpointDTO.driver, checkpointDTO.facility))
                .thenReturn(lastCheckin);

        // When / Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> checkpointService.checkout(checkpointDTO));
        assertEquals("Invalid date", exception.getMessage());

        Mockito.verify(checkpointPort, never()).saveCheckout(any(Checkout.class));  // No debería llamar a saveCheckout
    }

}