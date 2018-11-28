package jp.kusumotolab.kgenprog.ga;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import jp.kusumotolab.kgenprog.Configuration;
import jp.kusumotolab.kgenprog.project.factory.TargetProject;
import jp.kusumotolab.kgenprog.project.factory.TargetProjectFactory;
import jp.kusumotolab.kgenprog.testutil.TestUtil;

public class DUChainDistanceValidationTest {

  @Test
  public void testExecSingleClass() {
    final Path rootPath = Paths.get("example/refactoring/Max3");
    final TargetProject targetProject = TargetProjectFactory.create(rootPath);
    final Configuration config = new Configuration.Builder(targetProject).build();
    final Variant initialVariant = TestUtil.createVariant(config);

    final MetricValidation validation = new DUChainDistanceValidation();
    final Fitness fitness =
        validation.exec(initialVariant.getGeneratedSourceCode(), initialVariant.getTestResults());

    final double expected = 3.0 + 3.0 + 3.0 + 3.0 + 2.0;
    assertThat(fitness.getValue()).isCloseTo(expected, within(0.001));
  }

  @Test
  public void testExecMultipleClasses() {
    final Path rootPath = Paths.get("example/refactoring/GeometricMean");
    final TargetProject targetProject = TargetProjectFactory.create(rootPath);
    final Configuration config = new Configuration.Builder(targetProject).build();
    final Variant initialVariant = TestUtil.createVariant(config);

    final MetricValidation validation = new DUChainDistanceValidation();
    final Fitness fitness =
        validation.exec(initialVariant.getGeneratedSourceCode(), initialVariant.getTestResults());

    final double expected = 4.0 + 0.0;
    assertThat(fitness.getValue()).isCloseTo(expected, within(0.001));

  }
}