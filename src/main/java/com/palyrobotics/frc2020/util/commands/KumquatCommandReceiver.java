package com.palyrobotics.frc2020.util.commands;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.palyrobotics.frc2020.config.RobotState;

import java.io.IOException;

public class KumquatCommandReceiver {

    private static final int PORT = 5808, SOCKET_RETRY_MS = 500, BUFFER_SIZE = 50000;
    private static final String ADDRESS = "10.0.8.88";

    private final Client mDataClient;

    public KumquatCommandReceiver() {
        mDataClient = new Client(BUFFER_SIZE, BUFFER_SIZE);
        mDataClient.getKryo().register(double[].class);
        mDataClient.start();
        mDataClient.addListener(new Listener() {

            @Override
            public void connected(Connection connection) {
                System.out.println("Connected to stream");
                RobotState.getInstance().feedAvailable = true;
            }

            @Override
            public void disconnected(Connection connection) {
                RobotState.getInstance().feedAvailable = false;
                try {
                    mDataClient.reconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void received(Connection connection, Object message) {
                if (message instanceof double[]) {
                    double[] data = (double[]) message;
                    RobotState.getInstance().dxFromPowerCell = data[0] == 160 ? 0 : data[0];
                    RobotState.getInstance().detectedObjectArea = data[1];
//                    System.out.println(RobotState.getInstance().detectedObjectArea);
                }
            }
        });
    }

    public void tryConnect() {
        if (!mDataClient.isConnected()) {
            try {
                mDataClient.connect(SOCKET_RETRY_MS, ADDRESS, PORT, PORT);
            } catch (IOException connectException) {
                connectException.printStackTrace();
            }
        }
    }
}
