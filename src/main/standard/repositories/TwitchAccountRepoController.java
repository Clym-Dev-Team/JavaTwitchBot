package main.standard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@SuppressWarnings("unused")
public class TwitchAccountRepoController {

    public static TwitchAccountRepo TwitchAccountRepo;

    @Autowired
    public void setTwitchAccountRepo(TwitchAccountRepo twitchAccountRepo) {
        TwitchAccountRepo = twitchAccountRepo;
    }

}

