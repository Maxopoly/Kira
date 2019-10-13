package com.github.maxopoly.kira.api.input.packets;

import java.util.regex.Pattern;

import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.input.APIInput;
import com.github.maxopoly.kira.api.input.APISupplier;
import com.github.maxopoly.kira.api.sessions.APIIngameCommandSession;

public class RunIngameCommand extends APIInput {

    private String commandRegex = "[a-zA-Z0-9_\\- !?\\.]+";

    public RunIngameCommand() {
        super("in-game");
    }

    @Override
    public void handle(JSONObject argument, APISupplier supplier) {
        
        if(argument.isNull("command")) {
            return;
        }
        
        String command = argument.getString("command");
        
        if (!Pattern.matches(commandRegex, command) || command.length() > 255) {
            return;
        }
        
        APIIngameCommandSession cmd = new APIIngameCommandSession(supplier, command);
        
        KiraMain.getInstance().getRequestSessionManager().request(cmd);
    }

}