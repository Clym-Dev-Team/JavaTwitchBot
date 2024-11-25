package talium.system.coinsWatchtime.chatter;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatterService {
    ChatterRepo chatterRepo;

    public ChatterService(ChatterRepo chatterRepo) {
        this.chatterRepo = chatterRepo;
    }

    public void addWatchtimeSeconds(List<String> chatterIds, int seconds) {
        chatterRepo.incrementChatterWatchtimeBySeconds(chatterIds, seconds);
    }

}
