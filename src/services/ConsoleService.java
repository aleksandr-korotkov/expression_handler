package services;

import java.util.Optional;

public interface ConsoleService {

    Optional<String> readExpression();

    void writeResultOfOperation(String result);
}
