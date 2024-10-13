package talium.system.inputSystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthUi {

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("inputsSum", HealthManager.inputStatus());
        model.addAttribute("inputsAll", HealthManager.allStatuses());
        return "health";
    }
}
