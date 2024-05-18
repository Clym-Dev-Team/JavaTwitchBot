package main.modules.donation_goal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Table(name = "donation_goals")
@Entity
@Component
public class DonationGoal {
    @Id
    String idName;
    String displayName;
    Currency currency;
    double targetAmount;
    double amountInGoal;
    boolean active;

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
