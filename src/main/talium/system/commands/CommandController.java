package talium.system.commands;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import talium.inputs.Twitch4J.TwitchUserPermission;
import talium.system.chatTrigger.cooldown.ChatCooldown;
import talium.system.chatTrigger.cooldown.CooldownType;
import talium.system.chatTrigger.persistence.MessagePattern;
import talium.system.chatTrigger.persistence.TriggerEntity;
import talium.system.chatTrigger.persistence.TriggerService;
import talium.system.stringTemplates.Template;

import java.util.List;

@RestController
@RequestMapping(value = "/commands", produces = "application/json")
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
        var list = triggerService.getAllTriggers().stream().map(TriggerEntity::toTriggerDTO).toList();
        return gson.toJson(list);
    }

    @GetMapping("/id/{triggerId}")
    ResponseEntity<String> getByTriggerId(@PathVariable String triggerId) {
        var gson = new Gson();
        var command = triggerService.getTriggersId(triggerId);
        if (command.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gson.toJson(command.get().toTriggerDTO()));
    }

}
