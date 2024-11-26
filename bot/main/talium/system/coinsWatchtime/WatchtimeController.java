package talium.system.coinsWatchtime;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import talium.inputs.Twitch4J.TwitchApi;
import talium.system.coinsWatchtime.chatter.Chatter;
import talium.system.coinsWatchtime.chatter.ChatterDTO;
import talium.system.coinsWatchtime.chatter.ChatterService;

@RestController
@RequestMapping(value = "/watchtime", produces = "application/json")
public class WatchtimeController {
    private final ChatterService chatterService;
    private final Gson gson = new Gson();

    @Autowired
    public WatchtimeController(ChatterService chatterService) {
        this.chatterService = chatterService;
    }

    @GetMapping("/top")
    String getTop() {
        var chatters = chatterService.getTopWatchtime();
        var dto = chatters.stream().map(Chatter::toChatterDto).toList();
        return gson.toJson(dto);
    }

    @GetMapping("/username/{username}")
    ResponseEntity<String> getForUsername(@PathVariable String username) {
        var twitchUser = TwitchApi.getUserByName(username);
        if (twitchUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var chatter = chatterService.getDataForChatter(twitchUser.get().getId());
        return ResponseEntity.ok(gson.toJson(chatter.toChatterDto()));
    }

    @PostMapping("/update")
    HttpStatus post(@RequestBody String body) {
        var chatterDto = gson.fromJson(body, ChatterDTO.class);
        var dbChatter = chatterService.getDataForChatter(chatterDto.twitchUserId());
        int secondsSinceLastCoinsGain = 0;
        // use 0 as default if user is not yet in DB
        if (dbChatter != null) {
            secondsSinceLastCoinsGain = dbChatter.secondsSinceLastCoinsGain;
        }
        chatterService.save(chatterDto.toChatter(secondsSinceLastCoinsGain));
        return HttpStatus.OK;
    }
}