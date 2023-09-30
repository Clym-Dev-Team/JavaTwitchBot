package main.system.panelSystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")
    public String getDefault(Model model) {
        model.addAttribute("pageName", "dashboard");
        model.addAttribute("displayName", "Dashboard");
        model.addAttribute("list", MenuItemList.getMenuList());
        return "home";
    }
    @GetMapping("/page/dashboard")
    public String getDashboard(Model model) {
        return "dashboard";
    }
}
