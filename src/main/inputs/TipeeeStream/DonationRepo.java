package main.inputs.TipeeeStream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface DonationRepo extends CrudRepository<DonationEvent, Long> {
    //TODO Da das eine Nativ Query ist muss hier manuell der Name der Database eingefügt werden
    @Query(value = "SELECT * FROM `[DB]`.donations WHERE donated_at = " +
            "(SELECT max(donated_at) FROM `[DB]`.donations)", nativeQuery = true)
    DonationEvent getMostRecent();

}
