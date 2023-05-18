package main.inputs.TipeeeStream;

import io.socket.client.IO;
import io.socket.client.Socket;
import main.system.inputSystem.Input;
import main.system.inputSystem.TwitchBotInput;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;

@Input
@Component
public class TipeeeInput implements TwitchBotInput {

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
    private static final Logger LOGGER = LoggerFactory.getLogger(TipeeeInput.class);

    @Override
    public boolean checkConfiguration() {
        return true;
    }

    @Override
    public boolean shutdown() {
        socket.close();
        LOGGER.info("Shut down Tipeee input");

        return true;
    }

    @Override
    public void run() {
        LOGGER.info("Stating TipeeeInput for Channel " + channelName);
        try {
            socket = IO.socket("https://sso-cf.tipeeestream.com:443?access_token=" + apiKey);

            socket.on("new-event", data -> {
                TipeeeEventHandler.handleDonationEvent(Arrays.toString(data));
            });

            socket.on(Socket.EVENT_CONNECT, objects -> {
                try {
                    socket.emit("join-room", new JSONObject()
                            .put("room", apiKey)
                            .put("username", channelName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            socket.connect();
            //Wait for the Socket to connect, because it will be opened in a new Thread and
            // so will otherwise not be done by the time we check if the starting has worked
            Instant end = Instant.now().plusSeconds(10);
            while (!socket.connected() || end.isBefore(Instant.now()))
                Thread.onSpinWait();

            LOGGER.info("Tipeee socket connected");
        } catch (URISyntaxException e) {
            LOGGER.error("Could not connect to Tipeee socket");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean running() {
        if (socket == null)
            return false;
        return socket.connected();
    }

    @Override
    public String threadName() {
        return "TipeeeWebsocket";
    }
}
