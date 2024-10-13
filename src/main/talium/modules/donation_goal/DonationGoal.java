package talium.modules.donation_goal;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Table(name = "goals-donation_goals")
@Entity
@Component
public class DonationGoal {
    @Id
    public String idName;
    public String displayName;
    public Currency currency;
    public double targetAmount;
    public double amountInGoal;
    public boolean active;
    @Column(unique = true)
    public int order;

    public DonationGoal(String idName, String displayName, Currency currency, double targetAmount, double amountInGoal, boolean active) {
        this.idName = idName;
        this.displayName = displayName;
        this.currency = currency;
        this.targetAmount = targetAmount;
        this.amountInGoal = amountInGoal;
        this.active = active;
    }

    public DonationGoal() {
    }

    @Autowired
    public void setRepo(DonationGoalRepo repo) {
        DonationGoal.repo = repo;
    }
    public static DonationGoalRepo repo;
}
