package com.github.maxopoly.kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.rabbit.RabbitInputSupplier;
import com.github.maxopoly.kira.relay.actions.NewPlayerAction;

public class NewPlayerMessage extends RabbitMessage {

    public NewPlayerMessage() {
        super("newplayer");
    }

    @Override
    public void handle(JSONObject json, RabbitInputSupplier supplier) {
        String player = json.getString("player");
        long timestamp = json.optLong("timestamp", System.currentTimeMillis());
        NewPlayerAction action = new NewPlayerAction(timestamp, player);
        KiraMain.getInstance().getAPISessionManager().handleNewPlayerMessage(action);
        KiraMain.getInstance().getGroupChatManager().applyToAll(chat -> {
            chat.sendNewPlayer(action);
        });
    }
}
