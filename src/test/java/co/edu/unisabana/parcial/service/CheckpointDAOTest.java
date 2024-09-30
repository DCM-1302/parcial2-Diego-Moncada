package co.edu.unisabana.parcial.service;

import co.edu.unisabana.parcial.controller.dto.CheckpointDTO;
import co.edu.unisabana.parcial.repository.sql.entity.Checkpoint;
import co.edu.unisabana.parcial.repository.sql.jpa.CheckpointRepository;
import co.edu.unisabana.parcial.service.model.Checkin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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


}