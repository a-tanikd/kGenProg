package jp.kusumotolab.kgenprog.ga.validation;

import jp.kusumotolab.kgenprog.project.GeneratedSourceCode;
import jp.kusumotolab.kgenprog.project.test.TestResults;

public abstract class MetricValidation implements SourceCodeValidation {

  @Override
  public Fitness exec(GeneratedSourceCode sourceCode, TestResults testResults) {
    if (!sourceCode.isGenerationSuccess()) {
      return new MetricFitness(Double.NaN, testResults.getSuccessRate());
    }
    final double fitness = calculateFitness(sourceCode);

    return new MetricFitness(fitness, testResults.getSuccessRate());
  }

  protected abstract double calculateFitness(GeneratedSourceCode sourceCode);
}
