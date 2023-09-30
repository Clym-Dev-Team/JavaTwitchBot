package main.modules.core.commandPage;

import jakarta.annotation.PostConstruct;
import main.system.commandSystem.repositories.Command;
import main.system.panelSystem.MenuItemList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class CommandPageController {

    @PostConstruct
    public void addToMenuItemList() {
        ArrayList<MenuItemList.MenuItem> commandsList = new ArrayList<>();
        commandsList.add(new MenuItemList.MenuItem("Custom", "custom"));
        commandsList.add(new MenuItemList.MenuItem("Special", "special"));
        commandsList.add(new MenuItemList.MenuItem("Spam", "spam"));
        commandsList.add(new MenuItemList.MenuItem("Default", "default"));
        MenuItemList.addMenuItem(new MenuItemList.MenuItem("Commands", "commands", commandsList));
    }

    @GetMapping("/commands")
    public String getCommands(Model model) {
        model.addAttribute("pageName", "commands");
        model.addAttribute("displayName", "Commands");
        model.addAttribute("list", MenuItemList.getMenuList());
        return "home";
    }

    @GetMapping("/page/commands")
    public String getPageCommands(Model model) {
        model.addAttribute("commands", Command.repo.findAll());
        return "commands";
    }
}
