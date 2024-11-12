package talium.inputs.TipeeeStream;

import talium.system.eventSystem.EventDispatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Currency;

public class TipeeeEventHandler {

    static Logger LOGGER = LoggerFactory.getLogger(TipeeeEventHandler.class);

    public static void handleDonationEvent(String eventString) {
        try {
            JSONObject event = new JSONArray(eventString)
                    .getJSONObject(0)
                    .getJSONObject("event");

            if (!event.getString("type").equals("donation")) {
                LOGGER.debug("Event ignored, not a DonationEvent");
                return;
            }

            DonationEvent donationEvent = parseToInstance(event);
            LOGGER.debug("Collected + Parsed DonationEvent: \n{}", donationEvent);
            EventDispatcher.dispatch(donationEvent);
        } catch (Exception e) {
            LOGGER.error("Parsing of Event Failed! Original: {}", eventString);
            e.printStackTrace();
        }
    }

    private static DonationEvent parseToInstance(JSONObject json) throws JSONException {
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
        String currency_code = json.getJSONObject("user").getJSONObject("currency").getString("code");
        Currency currency = Currency.getInstance(currency_code);

        String username = para.getString("username");
        return new DonationEvent(id, currency, amount, message, hasMessage, username, donated_at);
    }

}
