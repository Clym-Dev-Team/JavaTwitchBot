package main.modules.DesignBoard;

import jakarta.annotation.PostConstruct;
import main.system.panelSystem.MenuItemList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DesignBoardPageController {

    @PostConstruct
    public static void addToMenuItemList() {
        MenuItemList.addMenuItem(new MenuItemList.MenuItem("Design-Board", "designboard"));
    }

    @GetMapping("/designboard")
    public String getTest(Model model) {
        model.addAttribute("pageName", "designboard");
        model.addAttribute("displayName", "Design-Board");
        model.addAttribute("list", MenuItemList.getMenuList());
        return "home";
    }
    @GetMapping("/page/designboard")
    public String getPageTest(Model model) {
        return "designboard";
    }
}
