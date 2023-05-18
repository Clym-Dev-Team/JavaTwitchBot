package main.inputs.TipeeeStream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepo extends CrudRepository<DonationEvent, Long> {
    //TODO this is a nativ Query, the Table Name musst be changed Manually, if it changed!
    @Query(value = "SELECT * FROM donations WHERE donated_at = " +
            "(SELECT max(donated_at) FROM donations)", nativeQuery = true)
//    @Query(value = "SELECT d FROM Donations d WHERE d.donated_at = (SELECT MAX(d.donated_at) FROM Donations d)") Does not work
    DonationEvent getMostRecent();

}
