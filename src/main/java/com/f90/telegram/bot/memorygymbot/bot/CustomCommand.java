package com.f90.telegram.bot.memorygymbot.bot;

import java.util.Arrays;

public enum CustomCommand {

    START("/start"),
    STOP("/stop"),

    ADD("ADD"),
    DELETE("DELETE"),
    PLAY("PLAY"),
    TEST("TEST"),
    LEARN("LEARN"),
    UNKWOW(""),
    ;

    private final String value;

    CustomCommand(String value) {
        this.value = value;
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
}
