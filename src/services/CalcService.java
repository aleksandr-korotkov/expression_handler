package services;

import java.util.Deque;

public interface CalcService {

    String calcPostfixNotation(Deque<String> queue);

    Double calcExpression(Double operandOne , Double operandTwo, String operationSign);

    Deque<String> mapInfixNotationToPostfixNotation(String expression);

    void checkCountBrackets(String expression);
}
