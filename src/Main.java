import services.CalcService;
import services.CalcServiceImpl;
import services.ConsoleService;
import services.ConsoleServiceImpl;

import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        CalcService calcService = new CalcServiceImpl();
        ConsoleService consoleService = new ConsoleServiceImpl();
        String expression = consoleService.readExpression().get();
        calcService.checkCountBrackets(expression);
        Deque<String> postfixNotation = calcService.mapInfixNotationToPostfixNotation(expression);
        consoleService.writeResultOfOperation(calcService.calcPostfixNotation(postfixNotation));
    }
}
