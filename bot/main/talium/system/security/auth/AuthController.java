package talium.system.security.auth;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import talium.inputs.Twitch4J.Twitch4JInput;
import talium.system.security.auth.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {
    private final PanelUserRepo panelUserRepo;
    private final Gson gson = new Gson();

    @Autowired
    public AuthController(PanelUserRepo panelUserRepo) {
        this.panelUserRepo = panelUserRepo;
    }

    @GetMapping("/panelAccounts")
    String getAllPanelAccounts() {
        List<PanelUserDTO> list = new ArrayList<>();
        for (PanelUser p : panelUserRepo.findAll()) {
            var user = Twitch4JInput.getUserById(p.twitchUserId);
            String userName = null;
            if (user.isPresent()) {
                userName = user.get().getDisplayName();
            }
            list.add(new PanelUserDTO(userName, p.twitchUserId, p.accountCreationTime.toEpochMilli()));
        }
        return gson.toJson(list);
    }

    @PostMapping("/panelAccounts")
    HttpStatus addNewPanelAccount(@RequestBody String userName) {
        var userId = Twitch4JInput.getUserByName(userName);
        if (userId.isEmpty()) return HttpStatus.NOT_FOUND;
        panelUserRepo.save(new PanelUser(userId.get().getId()));
        return HttpStatus.CREATED;
    }

    @DeleteMapping("/panelAccounts")
    void removePanelAccount(@RequestBody String userId) {
        panelUserRepo.deleteById(userId);
    }

    @PostMapping("/deleteUserSessions")
    ResponseEntity<String> sessionReset(Authentication authentication) {
        PanelUser panelUser = (PanelUser) authentication.getDetails();
        if (panelUser != null) {
            Optional<Session> byPanelUser = SessionService.getByPanelUser(panelUser);
            byPanelUser.ifPresent(SessionService::deleteSession);
            return ResponseEntity.ok().build();
        }
        // delete all session with the user that owns this session
        Optional<Session> currentSession = SessionService.getByAccessToken((String) authentication.getPrincipal());
        if (currentSession.isEmpty()) {
            return new ResponseEntity<>("Authenticated, but User of this session could not be found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<Session> byPanelUser = SessionService.getByPanelUser(panelUser);
        byPanelUser.ifPresent(SessionService::deleteSession);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deleteAllSessions")
    void logoutAllUsers() {
        SessionService.deleteAllSessions();
    }

}
