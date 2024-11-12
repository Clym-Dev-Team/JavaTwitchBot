package talium.system.templateParser.statements;

import java.util.List;

public record LoopStatement(String name, String var, List<Statement> body) implements Statement {}
