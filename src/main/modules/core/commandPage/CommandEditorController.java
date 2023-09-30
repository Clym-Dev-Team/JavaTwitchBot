package main.modules.core.commandPage;

import main.system.commandSystem.repositories.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CommandEditorController {

    Logger LOGGER = LoggerFactory.getLogger(CommandEditorController.class);

    @GetMapping("/commandEditor")
    public String getCommandEditor(Model model,@RequestParam(name = "id") String id) {
        Optional<Command> commandOptional= Command.repo.findById(id);
        if (commandOptional.isEmpty()) {
            model.addAttribute("error", "Command with ID (uniqueName): " + id + " could not be found!");
            return "error";
        }
        model.addAttribute("command", commandOptional.get());
        return "commandEditor";
    }
}
