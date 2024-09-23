package main.system.templateParser.statements;

import main.system.templateParser.tokens.Comparison;

import java.util.List;

public record IfStatement(Comparison comparison, List<Statement> then, List<Statement> other) implements Statement {}
