package services;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcServiceImpl implements CalcService {

    @Override
    public String calcPostfixNotation(Deque<String> queue){
        Stack<Double> stack = new Stack<>();

        for (String elementOfQueue : queue){
            //Если входящий элемент является числом, помещаем его в стек.
            if(elementOfQueue.matches("\\d+")){
                stack.push(Double.parseDouble(elementOfQueue));
                continue;
            }
            //Если входящий элемент является оператором (*-/+), получаем два последних числа из стека и выполняем соответствующую операцию.
            // Далее помещаем полученный результат в стек.
            if(elementOfQueue.matches("[\\+\\-\\*\\/]")){
                stack.push(calcExpression(stack.pop(),stack.pop(),elementOfQueue));
            }
        }
        return String.valueOf(stack.peek());
    }

    @Override
    public Double calcExpression(Double operandOne , Double operandTwo, String operationSign){
        Double resultOfOperation;
        switch (operationSign){
            case ("-"): {
                resultOfOperation = operandTwo - operandOne;
                break;
            }
            case ("+"): {
                resultOfOperation = operandTwo + operandOne;
                break;
            }
            case ("*"): {
                resultOfOperation = operandTwo * operandOne;
                break;
            }
            case ("/"): {
                resultOfOperation = operandTwo / operandOne;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + operationSign);
        }
        return resultOfOperation;
    }

    @Override
    public Deque<String> mapInfixNotationToPostfixNotation(String expression) {
        Deque<String> queue = new ArrayDeque<>();
        Stack<String> stack = new Stack<>();
        char[] sequence = expression.replaceAll(" ","").toCharArray();
        Boolean lastSymbolInQueueIsNumber = false;

        for (int i = 0; i < sequence.length; i++) {
            //Если входящий элемент является левой скобкой, помещаем его в стек.
            if (sequence[i] == '(') {
                stack.push(String.valueOf(sequence[i]));
                lastSymbolInQueueIsNumber = false;
                continue;
            }

            //Если входящий элемент является правой скобкой, выгружаем стек и добавляем его элементы в очередь,
            // пока не увидим левую круглую скобку. Удаляем найденную скобку из стека.
            if (sequence[i] == ')') {
                while (!stack.peek().equals("(")) {
                    queue.add(stack.pop());
                }
                stack.pop();
                lastSymbolInQueueIsNumber = false;
                continue;
            }

            //Если входящий элемент число, то добавляем его в очередь.
            if (String.valueOf(sequence[i]).matches("\\d")) {
                if(lastSymbolInQueueIsNumber){
                    queue.add(queue.pollLast() + sequence[i]);
                    continue;
                }
                queue.add(String.valueOf(sequence[i]));
                lastSymbolInQueueIsNumber = true;
                continue;
            }

            //Если входящий элемент оператор (+, -, *, /)
            if (String.valueOf(sequence[i]).matches("[\\+\\-\\*\\/]")) {

                //Если стек пуст или содержит левую скобку в вершине стека, то добавляем входящий оператор в стек.
                if (stack.isEmpty() || stack.peek().equals("(")) {
                    stack.push(String.valueOf(sequence[i]));
                    lastSymbolInQueueIsNumber = false;
                    continue;
                }

                //Если входящий оператор имеет более высокий приоритет чем вершина стека, помещаем его в стек.
                if(sequence[i]=='*'||sequence[i]=='/'){

                    if(stack.peek().equals("+")||stack.peek().equals("-")){
                        stack.push(String.valueOf(sequence[i]));
                        lastSymbolInQueueIsNumber = false;
                        continue;
                    }

                    //Если входящий оператор имеет более низкий или равный приоритет, чем на вершине стека, выгружаем стек в очередь,
                    //пока не увидим оператор с меньшим приоритетом или левую скобку на вершине стека, затем добавляем входящий оператор в стек.
                    if(stack.peek().equals("*")||stack.peek().equals("/")){
                        while (true){
                            if(stack.empty()||stack.peek().equals("-")||stack.peek().equals("+")||stack.peek().equals("(")){
                                break;
                            }
                            queue.add(stack.pop());
                        }
                        stack.push(String.valueOf(sequence[i]));
                        lastSymbolInQueueIsNumber = false;
                        continue;
                    }
                }

                //Если входящий оператор имеет более низкий или равный приоритет, чем на вершине стека, выгружаем POP в очередь ,
                //пока не увидим оператор с меньшим приоритетом или левую скобку на вершине стека, затем добавляем входящий оператор в стек.
                if(sequence[i]=='-'||sequence[i]=='+'){
                    for (int j = stack.size()-1; j >= 0; j--) {
                        if(stack.peek().equals("(")){
                            break;
                        }
                        queue.add(stack.pop());
                    }
                    stack.push(String.valueOf(sequence[i]));
                    lastSymbolInQueueIsNumber = false;
                }
            }
        }

        for (int i = 0; i <= stack.size(); i++) {
            queue.add(stack.pop());
        }
        return queue;
    }

    @Override
    public void checkCountBrackets(String expression) {
        Matcher matcherOpenBracket =  Pattern.compile("\\(").matcher(expression);
        Matcher matcherCloseBracket =  Pattern.compile("\\)").matcher(expression);
        int countOpenBrackets = 0;
        int countCloseBrackets = 0;
        while (matcherOpenBracket.find()){
            countOpenBrackets++;
        }
        while (matcherCloseBracket.find()){
            countCloseBrackets++;
        }
        if(countCloseBrackets!=countOpenBrackets){
            throw new IllegalArgumentException("Несовпадение количества скобок!");
        }
    }
}
