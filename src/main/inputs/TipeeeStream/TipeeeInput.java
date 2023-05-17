package main.inputs.TipeeeStream;

import main.system.commandSystem.repositories.Command;
import main.system.commandSystem.repositories.CooldownType;
import main.system.commandSystem.repositories.TwitchUserPermissions;
import main.system.eventSystem.EventDispatcher;
import main.system.inputSystem.Input;
import main.system.inputSystem.TwitchBotInput;
import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;

@Input
@Component
public class TipeeeInput implements TwitchBotInput {

    private static final Logger logger = LoggerFactory.getLogger(TipeeeInput.class);

    //TODO Soll in der DB gespeichert werden
    @Value("${tipeee_apiKey}")
    public void setTipeeeApiKey(String tipeeeApiKey) {
        TipeeeInput.tipeeeApiKey = tipeeeApiKey;
    }

    private static String tipeeeApiKey;

    private final OkHttpClient http = new OkHttpClient();
    private final Request request = buildQuery();
    private boolean shouldRun = true;
    private boolean running = false;
    DonationEvent oldResponse;


    public TipeeeInput() {
    }


    @Override
    public boolean checkConfiguration() {
        HashSet<TwitchUserPermissions> permissions = new HashSet();
        permissions.add(TwitchUserPermissions.MODERATOR);

        Command command = new Command(
                "donationAlert",
                "Donation Event Command for the Tokio Stream",
                "!donationAlert",
                false,
                permissions,
                "/me clymMoney clymMoney Danke für die Donation von {lastDonationAmount}, {lastDonationName} clymPride \"{lastDonationMessage}\" Goal: {goal}",
                true,
                true,
                0,
                CooldownType.SECONDS,
                "HotPink"
        );
        if (!Command.repo.existsByMatcherStringIgnoreCase("!donationAlert")) {
            Command.repo.save(command);
        }
        return true;
    }

    @Override
    public boolean running() {
        return running;
    }

    @Override
    public boolean shutdown() {
        shouldRun = false;
        return true;
    }

    @Override
    public void run() {
        oldResponse = queryAPI(request);
        //Fake zum testen
        oldResponse = new DonationEvent(540140976, "EUR", "€", "Euro", 10.D, "Bottest", true, "CJ0Black", ZonedDateTime.parse("2023-03-13T00:20:40+01:00"));

        new Thread(() -> {
            while (shouldRun) {
                DonationEvent response = queryAPI(request);

                //Check if it is the same donation
                if (oldResponse != null) {
                    if (oldResponse.donated_at.compareTo(response.donated_at) >= 0) {
                        continue;
                    }
                }
                logger.debug("OLD: \n" + oldResponse);

                oldResponse = response;

                logger.debug("NEW: \n" + response);

                EventDispatcher.dispatch(response);
                DonationEvent.repo.save(response);
            }
            running = false;
        }, "TipeeeAPIHandler").start();
        running = true;
    }

    private Request buildQuery() {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.tipeeestream.com")
                .addPathSegments("v1.0/events.json")
                .addQueryParameter("apiKey", tipeeeApiKey)
                .addEncodedQueryParameter("type[]", "donation")
                .addQueryParameter("limit", "1").build();
        return new Request(httpUrl, "GET", Headers.of("Accept", "json"), null, Map.of());
    }

    private DonationEvent queryAPI(Request request) {

        JSONObject jsonObject;
        try (Response tryResponse = http.newCall(request).execute()) {
            jsonObject = new JSONObject(tryResponse.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        JSONObject firstEntry = jsonObject
                .getJSONObject("datas")
                .getJSONArray("items")
                .getJSONObject(0);

        return parseEntry(firstEntry);
    }

    private static DonationEvent parseEntry(JSONObject json) {
        JSONObject para = json.getJSONObject("parameters");

        String message;
        boolean hasMessage;
        try {
            message = para.getString("message");
            hasMessage = true;
        } catch (Exception ignored) {
            message = "";
            hasMessage = false;
        }

        String createdAt = json.getString("created_at");
        ZonedDateTime donated_at = ZonedDateTime.parse(createdAt.substring(0, createdAt.length() - 2));

        long id = json.getLong("id");
        double amount = para.getDouble("amount");
        String currency_label = json.getJSONObject("user").getJSONObject("currency").getString("label");
        String currency_symbol = json.getJSONObject("user").getJSONObject("currency").getString("symbol");
        String currency_code = json.getJSONObject("user").getJSONObject("currency").getString("code");
        String username = para.getString("username");

        return new DonationEvent(id, currency_code, currency_symbol, currency_label, amount, message, hasMessage, username, donated_at);
    }

    @Override
    public String threadName() {
        return "TipeeeAPI_Endpoint";
    }

}
