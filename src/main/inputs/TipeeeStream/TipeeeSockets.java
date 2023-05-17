package main.inputs.TipeeeStream;

import io.socket.client.IO;
import io.socket.client.Socket;
import main.system.inputSystem.TwitchBotInput;

import java.net.URISyntaxException;
import java.util.Arrays;

//@Input
public class TipeeeSockets implements TwitchBotInput {

    //Placeholder
    private final String tipeeeApiKey = "[APIKEY]";


    @Override
    public boolean checkConfiguration() {
        return true;
    }

    @Override
    public boolean shutdown() {
        return true;
    }

    @Override
    public void run() {
        try {
            Socket socket = IO.socket("https://sso-cf.tipeeestream.com", IO.Options.builder()
                    .setQuery("access_token=\"" + tipeeeApiKey + "\"")
                            .setPort(443)
                            .setTransports(new String[]{"websocket"})
                    .build());
            socket.onAnyIncoming(objects -> System.out.println("IN = " + Arrays.toString(objects)));
            socket.onAnyOutgoing(objects -> System.out.println("OUT = " + Arrays.toString(objects)));
            socket.connect();

            System.out.println("socket.connected() = " + socket.connected());
//            socket.on("connect", objects -> {
//                socket.emit()
//            });



            socket.on(Socket.EVENT_CONNECT, objects -> {
//                socket.emit("join-room");
//                    socket.emit('join-room', {
//                            room: 'YOUR_API_KEY',
//                    username: 'YOUR_USERNAME'
//                    })
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
                System.out.println("Error = " + Arrays.toString(objects));
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean running() {
        return true;
    }

    @Override
    public String threadName() {
        return "TipeeeSockets_Endpoint";
    }
}

