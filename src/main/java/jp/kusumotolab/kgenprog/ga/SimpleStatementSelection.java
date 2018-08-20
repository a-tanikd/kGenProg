package jp.kusumotolab.kgenprog.ga;

import java.util.List;
import java.util.Random;
import org.eclipse.jdt.core.dom.Statement;
import jp.kusumotolab.kgenprog.project.GeneratedAST;

public class SimpleStatementSelection implements CandidateSelection {

  private final Random random;
  private StatementVisitor visitor;

  public SimpleStatementSelection(
      Random random) {
    this.random = random;
  }

  @Override
  public void setCandidates(List<GeneratedAST> candidates) {
    visitor = new StatementVisitor(candidates);
  }

  @Override
  public Statement exec() {
    final List<Statement> statements = visitor.getStatements();
    final int index = random.nextInt(statements.size());
    return statements.get(index);
  }
}
