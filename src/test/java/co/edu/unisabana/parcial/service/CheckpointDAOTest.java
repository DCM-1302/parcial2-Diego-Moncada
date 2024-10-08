package co.edu.unisabana.parcial.service;

import co.edu.unisabana.parcial.controller.dto.CheckpointDTO;
import co.edu.unisabana.parcial.repository.sql.entity.Checkpoint;
import co.edu.unisabana.parcial.repository.sql.jpa.CheckpointRepository;
import co.edu.unisabana.parcial.service.model.Checkin;
import co.edu.unisabana.parcial.service.model.Checkout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckpointDAOTest {

    @Mock
    private CheckpointRepository checkpointRepository;

    @InjectMocks
    private CheckpointDAO checkpointDAO;

    @Test
    void testSaveCheckin() {
        String driver = "driver";
        String facility = "facility";
        Checkin checkin = new Checkin(facility,driver,25);
        Checkpoint checkpoint = Checkpoint.fromCheckin(checkin);

        checkpointDAO.saveCheckin(checkin);

        Mockito.verify(checkpointRepository, Mockito.times(1)).save(Mockito.any(Checkpoint.class));
    }

    @Test
    void testSaveCheckout() {

        Checkout checkout = new Checkout("facility","driver",25);
        Checkpoint checkpoint = Checkpoint.fromCheckout(checkout);

        checkpointDAO.saveCheckout(checkout);

        Mockito.verify(checkpointRepository, Mockito.times(1)).save(Mockito.any(Checkpoint.class));
    }


    @Test
    void testFindLastCheckin_Found() {

        String driver = "driver1";
        String facility = "facility1";
        Checkpoint checkpoint = new Checkpoint();
        Mockito.when(checkpointRepository.findFirstByDriverAndFacilityAndFinalizedIsFalse(driver, facility))
                .thenReturn(Optional.of(checkpoint));

        Checkin result = checkpointDAO.findLastCheckin(driver, facility);

        Assertions.assertNotNull(result);
        Mockito.verify(checkpointRepository, Mockito.times(1))
                .findFirstByDriverAndFacilityAndFinalizedIsFalse(driver, facility);
    }

    @Test
    void testFindLastCheckin_NotFound() {

        String driver = "driver1";
        String facility = "facility1";
        Mockito.when(checkpointRepository.findFirstByDriverAndFacilityAndFinalizedIsFalse(driver, facility))
                .thenReturn(Optional.empty());

        Checkin result = checkpointDAO.findLastCheckin(driver, facility);

        Assertions.assertNull(result);
        Mockito.verify(checkpointRepository, Mockito.times(1))
                .findFirstByDriverAndFacilityAndFinalizedIsFalse(driver, facility);
    }

    @Test
    void testFinishCheckin() {

        Checkin checkin = new Checkin("facility","driver",25);
        checkin.setId(1);
        Checkpoint checkpoint = new Checkpoint();
        Mockito.when(checkpointRepository.findById(1)).thenReturn(Optional.of(checkpoint));

        checkpointDAO.finishCheckin(checkin);

        Assertions.assertTrue(checkpoint.isFinalized());
        Mockito.verify(checkpointRepository, Mockito.times(1)).findById(1);
        Mockito.verify(checkpointRepository, Mockito.times(1)).save(checkpoint);
    }

    @Test
    void testFinishCheckin_NotFound() {

        Checkin checkin = new Checkin("facility","driver",25);
        checkin.setId(1);
        Mockito.when(checkpointRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> checkpointDAO.finishCheckin(checkin));
        Mockito.verify(checkpointRepository, Mockito.times(1)).findById(1);
        Mockito.verify(checkpointRepository, Mockito.never()).save(Mockito.any(Checkpoint.class));  // No se debería llamar a save
    }


}