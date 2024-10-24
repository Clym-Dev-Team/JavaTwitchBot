package talium.system.chatTrigger.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePatternService {
    private final MessagePatternRepo repo;

    @Autowired
    public MessagePatternService(MessagePatternRepo repo) {
        this.repo = repo;
    }

    void save(MessagePattern pattern) {
        repo.save(pattern);
    }
}
