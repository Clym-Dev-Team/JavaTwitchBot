package talium.modules.donation_goal;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import talium.system.stringTemplates.Template;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import talium.system.stringTemplates.TemplateService;

import java.util.Currency;
import java.util.Optional;

@RestController
@RequestMapping("/donation_goals")
public class MVP_GoalController {

    private final DonationGoalRepo donationGoalRepo;
    private final TemplateService templateService;

    @Autowired
    public MVP_GoalController(DonationGoalRepo donationGoalRepo, TemplateService templateService) {
        this.donationGoalRepo = donationGoalRepo;
        this.templateService = templateService;
    }

    @GetMapping
    String donationGoal() {
        Optional<DonationGoal> goal = DonationGoal.repo.findById("goal");
        Optional<Template> template = templateService.getTemplateById("alerts.tipeee.donation");
        if (template.isEmpty() || goal.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
            //TODO
        }
        DonationGoalJson goalJson = new DonationGoalJson(
                goal.get().displayName,
                goal.get().targetAmount,
                goal.get().amountInGoal,
                template.get().template
        );
        Gson gson = new Gson();
        return gson.toJson(goalJson);
    }

    @PostMapping
    void donationGoal(@RequestBody String donationGoal) {
        Gson gson = new Gson();
        DonationGoalJson goalJson = gson.fromJson(donationGoal, DonationGoalJson.class);
        Optional<DonationGoal> goal = DonationGoal.repo.findById("goal");
        DonationGoal dg;
        if (goal.isPresent()) {
            dg = goal.get();
            dg.targetAmount = goalJson.target();
            dg.amountInGoal = goalJson.current();
            dg.displayName = goalJson.name();
        } else {
            dg = new DonationGoal(
                    "goal",
                    goalJson.name(),
                    Currency.getInstance("EUR"),
                    goalJson.target(),
                    goalJson.current(),
                    true
            );
        }
        donationGoalRepo.save(dg);
        templateService.updateTemplateStringById(goalJson.alertText(), "alerts.tipeee.donation");
    }
}
