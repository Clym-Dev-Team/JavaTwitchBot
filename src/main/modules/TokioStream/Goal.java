package main.modules.TokioStream;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Table( name = "Tokio-Donation_Goal")
@Component
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;
    public String key;
    public double targetAmount;
    public double currentAmount;

    public Goal() {
        this.key = "primary";
    }

    public Goal(double targetAmount, double currentAmount) {
        this.key = "primary";
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    @Autowired
    public void setGoalRepo(GoalRepo goalRepo) {
        Goal.repo = goalRepo;
    }
    public static GoalRepo repo;

}
