package main.modules.TokioStream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GoalRepo extends CrudRepository<Goal, Long> {
    @Transactional
    @Modifying
    @Query("update Goal g set g.currentAmount = ?1 where g.key = ?2")
    void updateCurrentAmountByKey(double currentAmount, String key);
    Goal findByKey(String key);

    Goal getByCurrentAmount(double currentAmount);

    double getCurrentAmountByKey(String key);



}
