package budgetmanager.console;

import java.util.Arrays;

enum InputRecognizer {

    EMPTY("^$"),
    ADD_PURCHASE("(?i)add\\s+(food|clothes|entertainment|other)"),
    DELETE_PURCHASE("(?i)delete\\s+'\\w+'\\s+([0-9]{4}-[0-9]{2}-[0-9]{2})"),
    GET_PURCHASES("(?i)get\\s+(all|food|clothes|entertainment|other)" +
                        "(\\s+(last-week|month|year))?" +
                        "(\\s+(oldest|recent|cheapest|expensive))?"),
    ADD_BALANCE("(?i)add\\s+balance\\s+[0-9]+[,.]?[0-9]+"),
    GET_BALANCE("(?i)get\\s+balance"),
    HELP("(?i)help"),
    EXIT("(?i)exit"),
    INCORRECT(".*");

    private final String PATTERN;

    InputRecognizer(String pattern) {
        this.PATTERN = pattern;
    }

    static InputRecognizer recognize(String line) {
        return Arrays.stream(InputRecognizer.values())
                .filter(x -> line.matches(x.PATTERN))
                .findFirst()
                .get();
    }
}
