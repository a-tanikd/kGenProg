package jp.kusumotolab.kgenprog.ga.crossover;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import jp.kusumotolab.kgenprog.ga.variant.Gene;
import jp.kusumotolab.kgenprog.ga.variant.HistoricalElement;
import jp.kusumotolab.kgenprog.ga.variant.Variant;

public class TestSimilarityBasedSinglePointCrossoverTest {


  private static CrossoverTestVariants testVariants;

  @Before
  public void setup() {
    testVariants = new CrossoverTestVariants();
  }

  @Test
  public void test_exec1() {

    // 生成するバリアントを制御するための疑似乱数
    final Random random = Mockito.mock(Random.class);
    when(random.nextBoolean()).thenReturn(true);
    when(random.nextInt(anyInt())).thenReturn(0);

    // バリアントの生成
    final Crossover crossover = new TestSimilarityBasedSinglePointCrossover(random, 1);
    final List<Variant> variants = crossover.exec(testVariants.variantStore);
    final Variant variant = variants.get(0);

    // 1つ目のバリアントとしてvariantA，2つ目のバリアントとしてvariantBが選ばれているはず
    final HistoricalElement element = variant.getHistoricalElement();
    assertThat(element.getParents()).containsExactly(testVariants.variantA, testVariants.variantB);

    // 交叉ポイントは1なので，0はvariantAと同じ，123はvariantBと同じになっているはず
    final Gene gene = variant.getGene();
    assertThat(gene.getBases()).containsExactly(testVariants.noneBase, testVariants.noneBase,
        testVariants.noneBase, testVariants.insertBase);
  }

  @Test
  public void test_exec2() {

    // 生成するバリアントを制御するための疑似乱数
    final Random random = Mockito.mock(Random.class);
    when(random.nextBoolean()).thenReturn(false);
    when(random.nextInt(anyInt())).thenReturn(2);

    // バリアントの生成
    final Crossover crossover = new TestSimilarityBasedSinglePointCrossover(random, 1);
    final List<Variant> variants = crossover.exec(testVariants.variantStore);
    final Variant variant = variants.get(0);

    // 1つ目のバリアントとしてvariantC，2つ目のバリアントとしてvariantDが選ばれているはず
    final HistoricalElement element = variant.getHistoricalElement();
    assertThat(element.getParents()).containsExactly(testVariants.variantC, testVariants.variantD);

    // 交叉ポイントは3なので，012はvariantCと同じ，3はvarriantDと同じになっているはず
    final Gene gene = variant.getGene();
    assertThat(gene.getBases()).containsExactly(testVariants.noneBase, testVariants.insertBase,
        testVariants.insertBase, testVariants.insertBase);
  }
}
