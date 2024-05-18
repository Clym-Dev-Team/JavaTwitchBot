package talium.inputs.TipeeeStream;

import io.socket.client.IO;
import io.socket.client.Socket;
import talium.TwitchBot;
import talium.system.UnexpectedShutdownException;
import talium.system.inputSystem.BotInput;
import talium.system.inputSystem.HealthManager;
import talium.system.inputSystem.Input;
import talium.system.inputSystem.InputStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;

@Input
public class TipeeeInput implements BotInput {

    public static final String TIPEEE_API_URL = "https://sso-cf.tipeeestream.com:443?access_token=";

    @Value("${tipeee_apiKey}")
    public void setApiKey(String apiKey) {
        TipeeeInput.apiKey = apiKey;
    }
    private static String apiKey;

    @Value("${tipeee_channel}")
    public void setChannelName(String channelName) {
        TipeeeInput.channelName = channelName;
    }
    private static String channelName;

    private Socket socket;
    private InputStatus health;
    private static final Logger LOGGER = LoggerFactory.getLogger(TipeeeInput.class);

    @Override
    public void shutdown() {
        socket.close();
        LOGGER.info("Shut down Tipeee input");
    }

    @Override
    public void run() {
        report(InputStatus.STARTING);
        LOGGER.info("Stating TipeeeInput for Channel {}", channelName);
        try {
            socket = IO.socket(TIPEEE_API_URL + apiKey);

            socket.on("new-event", data -> TipeeeEventHandler.handleDonationEvent(Arrays.toString(data)));

            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
                report(InputStatus.DEAD);
                throw new RuntimeException(Arrays.toString(objects));
            });

            socket.on(Socket.EVENT_CONNECT, objects -> {
                try {
                    socket.emit("join-room", new JSONObject()
                            .put("room", apiKey)
                            .put("username", channelName));
                    report(InputStatus.HEALTHY);
                } catch (JSONException e) {
                    report(InputStatus.DEAD);
                    throw new RuntimeException(e);
                }
            });

            socket.connect();
            //Wait for the Socket to connect, because it will be opened in a new Thread and
            // so will otherwise not be done by the time we check if the starting has worked
            Instant end = Instant.now().plusSeconds(10);
            while (!socket.connected() || end.isBefore(Instant.now()) && !TwitchBot.requestedShutdown)
                Thread.onSpinWait();

            if (TwitchBot.requestedShutdown) {
                throw new UnexpectedShutdownException();
            }

            LOGGER.info("Tipeee socket connected");
        } catch (URISyntaxException e) {
            LOGGER.error("Could not connect to Tipeee socket");
            report(InputStatus.DEAD);
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStatus getHealth() {
        return health;
    }

    @Override
    public String threadName() {
        return "TipeeeWebsocket";
    }

    private void report(InputStatus health) {
        HealthManager.reportStatus(this, health);
        this.health = health;
    }
}
