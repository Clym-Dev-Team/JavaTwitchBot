package main.inputs.tipeee;

import io.socket.client.IO;
import io.socket.client.Socket;
import main.system.inputSystem.TipeeStreamInput;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class TipeeInput implements TipeeStreamInput {

    private String apiKey;
    private String channelName;
    private Socket socket;
    private static final Logger LOGGER = LoggerFactory.getLogger(TipeeInput.class);

    @Override
    public boolean checkConfiguration() {
        return false;
    }

    @Override
    public boolean shutdown() {
        if (socket.isActive()) {
            socket.close();
            LOGGER.info("Shut down Tipeee input");
        }

        return true;
    }

    @Override
    public void run() {
        try {
            socket = IO.socket("https://sso-cf.tipeeestream.com:443?access_token=" + apiKey);
            socket.on("new-event", objects -> {

            });
            socket.on("connect", objects -> {
                try {
                    socket.emit("join-room", new JSONObject()
                            .put("room", apiKey)
                            .put("username", channelName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            socket.connect();
            LOGGER.info("Tipeee socket connected");
        } catch (URISyntaxException e) {
            LOGGER.error("Could not conntect Tipeee socket");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean running() {
        return socket.connected();
    }

    private void callEvent() {

    }
}
