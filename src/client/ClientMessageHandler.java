package client;

import java.io.Serializable;

import gamecore.Table;
import system.Message;

public class ClientMessageHandler {


    static void handleMessage(Message message) {

        switch(message.getCommand()) {
            case ("print"):
                System.out.println();
                for(Serializable arg : message.getArguments()) {
                    System.out.println(arg);
                }
                break;

            case ("play"):
                Client.play();
                break;

            case ("table"):
                System.out.println("Updating table");
                Client.localTable = (Table) message.getArguments().get(0);
                break;

            case ("update"):
                int column = (int) message.getArguments().get(0);
                int playerId = (int) message.getArguments().get(1);

                System.out.println("Recebeu msg na coluna " + column);
                Client.localTable.add(column, playerId);

                for (int i = 0; i < Client.localTable.getTable().length; i++) {
                    for (int j = 0; j < Client.localTable.getTable()[i].length; j++) {
                        System.out.print(Client.localTable.getTable()[i][j] + " ");
                    }
                    System.out.println();
                }
        }
    }
}