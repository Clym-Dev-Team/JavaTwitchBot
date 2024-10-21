package talium.system.commands;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import talium.system.chatTrigger.persistence.TriggerEntity;
import talium.system.stringTemplates.Template;

@Entity
@Table(name = "sys-twitchCommands")
public record TwitchCommand(
        @Id @OneToOne
        TriggerEntity trigger,
        @OneToOne
        Template template
) {
}
