package com.f90.telegram.bot.memorygymbot.bot;

public class Command {

    private final CustomCommand name;
    private final String value;

    public Command(CustomCommand name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Command fromText(String text) {
        return new Command(CustomCommand.getCommand(text), CustomCommand.getCommandValue(text));
    }

    public CustomCommand getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name=" + name +
                ", value='" + value + '\'' +
                '}';
    }
}
