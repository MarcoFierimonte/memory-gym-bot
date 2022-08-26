package com.f90.telegram.bot.memorygymbot.bot;

import java.util.Arrays;

public enum CustomCommand {

    ADD("/add "),
    DELETE("/delete "),
    LEARN("/learn "),
    UNKWOW(""),
    ;

    private final String value;

    CustomCommand(String value) {
        this.value = value;
    }

    public static CustomCommand getCommand(String input) {
        return Arrays.stream(CustomCommand.values()).filter(c -> input.startsWith(c.getValue())).findFirst().orElse(UNKWOW);
    }

    public static String getCommandValue(String input) {
        CustomCommand customCommand = getCommand(input);
        return input.split(customCommand.getValue())[1];
    }

    public String getValue() {
        return value;
    }
}
