package com.f90.telegram.bot.memorygymbot.bot;

public class Command {

    private final CustomCommand type;
    private final String value;

    public Command(CustomCommand type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Command fromText(String text) {
        return new Command(CustomCommand.getCommand(text), CustomCommand.getCommandValue(text));
    }

    public CustomCommand.CmdType getCmdType() {
        return type.getType();
    }

    public CustomCommand getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Command{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
