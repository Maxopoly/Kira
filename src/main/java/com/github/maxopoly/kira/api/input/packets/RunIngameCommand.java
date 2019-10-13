package com.github.maxopoly.kira.api.input.packets;

import java.util.regex.Pattern;

import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.input.APIInput;
import com.github.maxopoly.kira.api.input.APISupplier;
import com.github.maxopoly.kira.api.sessions.APIIngameCommandSession;

public class RunIngameCommand extends APIInput {

    public RunIngameCommand() {
        super("in-game");
    }

    @Override
    public void handle(JSONObject argument, APISupplier supplier) {
        if(argument.isNull("command") || argument.isNull("identifier")) {
            return;
        }
        
        String command = argument.getString("command");
        String id = argument.getString("id");
        
        if (command.length() > 255) {
            return;
        }
        
        APIIngameCommandSession cmd = new APIIngameCommandSession(supplier, command, id); 
        KiraMain.getInstance().getRequestSessionManager().request(cmd);
    }

}