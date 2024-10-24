package talium.system.commands;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import talium.inputs.Twitch4J.TwitchUserPermission;
import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.chatTrigger.cooldown.CooldownType;
import talium.system.chatTrigger.persistence.MessagePattern;
import talium.system.chatTrigger.persistence.TriggerEntity;
import talium.system.chatTrigger.persistence.TriggerService;
import talium.system.stringTemplates.Template;

import java.util.List;

@RestController
@RequestMapping("/commands")
public class CommandController {
    TriggerService triggerService;

    @Autowired
    public CommandController(TriggerService triggerService) {
        this.triggerService = triggerService;
        var pat = new MessagePattern("!test", false, true, true);
        var tem = new Template("bsp.template", "templateString", null);
        var tr = new TriggerEntity(
                "bsp.trigger",
                "",
                List.of(pat),
                TwitchUserPermission.EVERYONE,
                new ChatCooldown(CooldownType.MESSAGES, 0),
                new ChatCooldown(CooldownType.MESSAGES, 0),
                true,
                tem
        );
        triggerService.save(tr);
        System.out.println("saved!");
    }

    @GetMapping("/all")
    String getAllCommands() {
        var gson = new Gson();
        var list = triggerService.getAllTriggers();
        return gson.toJson(list);
    }

    @GetMapping("/id/{triggerId}")
    String getByTriggerId(@PathVariable String triggerId) {
        var gson = new Gson();
        var command = triggerService.getTriggersId(triggerId);
        return gson.toJson(command);
    }

}
