package standard.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface TwitchAccountRepo extends CrudRepository<TwitchAccount, String> {

    @NotNull ArrayList<TwitchAccount> findAll();
//    TwitchAccount findbyRole(String role);

//    void updatebytwitchUserID(String twitchUserID, TwitchAccount account);
}
