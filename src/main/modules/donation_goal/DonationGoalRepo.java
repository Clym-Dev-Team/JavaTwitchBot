package main.modules.donation_goal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationGoalRepo extends CrudRepository<DonationGoal, String> {

    DonationGoal findByIdName(String name);

    DonationGoal findByIdNameAndActive(String idName, boolean active);

    List<DonationGoal> findByActive(boolean active);
}
