package jp.kusumotolab.kgenprog.project.jdt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import jp.kusumotolab.kgenprog.project.ASTLocation;
import jp.kusumotolab.kgenprog.project.GeneratedAST;
import jp.kusumotolab.kgenprog.project.LineNumberRange;
import jp.kusumotolab.kgenprog.project.SourcePath;

/**
 * JDT AST の単一ノードを示すオブジェクト Operation のターゲットに利用する
 *
 * @author r-arima
 * @see jp.kusumotolab.kgenprog.JDTOperaion
 */
public class JDTASTLocation implements ASTLocation {

  public final ASTNode node;

  private final SourcePath sourcePath;
  private final GeneratedJDTAST<?> generatedAST;

  public JDTASTLocation(final SourcePath sourcePath, final ASTNode node, final GeneratedJDTAST<?> generatedAST) {
    this.node = node;
    this.sourcePath = sourcePath;
    this.generatedAST = generatedAST;
  }

  /**
   * 指定された ASTNode がこの Location が表現する ASTNode 内のどこに相当するかを返す．
   *
   * @param otherASTRoot この Location において対応する ASTNode を取得したい ASTNode
   * @return 指定された ASTNode がこの Location が表現する ASTNode 内で相当する ASTNode．
   */
  public ASTNode locate(final ASTNode otherASTRoot) {
    final List<TreePathElement> treePaths = new ArrayList<TreePathElement>();
    ASTNode currentNode = node;
    while (true) {
      final StructuralPropertyDescriptor locationInParent = currentNode.getLocationInParent();
      if (locationInParent == null) {
        break;
      }

      final ASTNode parent = currentNode.getParent();
      int idx = TreePathElement.NOT_LIST;

      if (locationInParent.isChildListProperty()) {
        // Listの場合、indexも覚えておく
        final List<?> children = (List<?>) parent.getStructuralProperty(locationInParent);
        idx = children.indexOf(currentNode);
      }

      treePaths.add(new TreePathElement(locationInParent, idx));

      currentNode = parent;
    }

    Collections.reverse(treePaths);

    currentNode = otherASTRoot;
    for (final TreePathElement path : treePaths) {
      currentNode = path.moveToChild(currentNode);
    }

    return currentNode;
  }

  private static class TreePathElement {

    public static final int NOT_LIST = -1;

    StructuralPropertyDescriptor descriptor;
    int idx;

    public TreePathElement(final StructuralPropertyDescriptor descriptor, final int idx) {
      this.descriptor = descriptor;
      this.idx = idx;
    }

    public ASTNode moveToChild(final ASTNode current) {
      final Object child = current.getStructuralProperty(descriptor);
      if (idx == NOT_LIST) {
        return (ASTNode) child;
      } else {
        return (ASTNode) ((List<?>) child).get(idx);
      }
    }
  }

  @Override
  public SourcePath getSourcePath() {
    return sourcePath;
  }

  @Override
  public LineNumberRange inferLineNumbers() {
    final ASTNode root = this.node.getRoot();

    if (!(root instanceof CompilationUnit)) {
      return ASTLocation.NONE;
    }

    final CompilationUnit compilationUnit = (CompilationUnit) root;

    final int start = compilationUnit.getLineNumber(this.node.getStartPosition());
    final int end =
        compilationUnit.getLineNumber(this.node.getStartPosition() + this.node.getLength());

    return new LineNumberRange(start, end);
  }

  @Override
  public GeneratedAST<?> getGeneratedAST() {
    return generatedAST;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JDTASTLocation that = (JDTASTLocation) o;
    return Objects.equals(node, that.node) &&
        Objects.equals(getSourcePath(), that.getSourcePath());
  }

  @Override
  public int hashCode() {
    return Objects.hash(node, getSourcePath());
  }

  public ASTNode getNode() {
    return node;
  }
}
