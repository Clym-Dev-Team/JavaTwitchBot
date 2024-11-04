package talium.system.inputSystem;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthUi {

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("inputsSum", HealthManager.inputStatus());
        model.addAttribute("inputsAll", HealthManager.allStatuses());
        return "health";
    }

    @GetMapping("/health/json")
    @ResponseBody
    public String healthJson() {
        Gson gson = new Gson();
        return gson.toJson(HealthManager.allStatuses());
    }
}
