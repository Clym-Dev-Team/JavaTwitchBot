package talium.system.chatTrigger.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talium.system.chatTrigger.triggerEngine.RuntimeTrigger;
import talium.system.commandSystem.repositories.ChatMessage;

import java.util.HashMap;

import static talium.system.chatTrigger.triggerEngine.TriggerProvider.transformTrigger;

@Service
public class TriggerService {
    private final TriggerRepo repo;

    @Autowired
    public TriggerService(TriggerRepo triggerRepo) {
        this.repo = triggerRepo;
    }

    public HashMap<String, TriggerEntity> getCodeTriggers() {
        HashMap<String, TriggerEntity> map = new HashMap<>();
        var list = repo.getAllByisUserTrigger(false);
        for (var item : list) {
            map.put(item.id(), item);
        }
        return map;
    }

    public HashMap<String, RuntimeTrigger> getUerTriggers() {
        HashMap<String, RuntimeTrigger> map = new HashMap<>();
        var list = repo.getAllByisUserTrigger(true);
        for (var item : list) {
            map.put(item.id(), transformTrigger(item, TriggerService::TESINGCALLBACK));
        }
        return map;
    }

    public void removeTrigger(String id) {
        repo.deleteById(id);
    }

    public static void TESINGCALLBACK(String triggerId, ChatMessage message) {
        System.out.println("CALLBACK " + triggerId + ": " + message);

    }
}
