package com.f90.telegram.bot.memorygymbot.bot;

import java.util.Arrays;

public enum CustomCommand {

    START("/start", CmdType.MENU),
    STOP("/stop", CmdType.MENU),

    // main menu keyboad buttons
    ADD("ADD", CmdType.MENU),
    DELETE("DELETE", CmdType.MENU),
    TEST("TEST", CmdType.MENU),
    LEARN("LEARN", CmdType.MENU),
    VERBS("VERBS", CmdType.MENU),

    UNKWOW("", CmdType.UNKNOW),
    ;

    private final String value;
    private final CmdType type;

    CustomCommand(String value, CmdType type) {
        this.value = value;
        this.type = type;
    }

    public static CustomCommand getCommand(String input) {
        return Arrays.stream(CustomCommand.values()).filter(c -> input.trim().startsWith(c.getValue().trim())).findFirst().orElse(UNKWOW);
    }

    public static String getCommandValue(String input) {
        CustomCommand customCommand = getCommand(input);
        String[] splitted = input.split(customCommand.getValue());
        return splitted.length > 1 ? splitted[1] : null;
    }

    public String getValue() {
        return value;
    }

    public CmdType getType() {
        return type;
    }

    public enum CmdType {
        BOT,
        MENU,
        ACTION,
        UNKNOW,
    }
}
