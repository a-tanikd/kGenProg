package jp.kusumotolab.kgenprog.project.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.kusumotolab.kgenprog.project.SourcePath;

public class MoveAfterOperation extends JDTOperation {

  private static final Logger log = LoggerFactory.getLogger(MoveAfterOperation.class);

  private final JDTASTLocation src;

  public MoveAfterOperation(final JDTASTLocation src) {
    this.src = src;
  }

  @Override
  protected <T extends SourcePath> void applyToASTRewrite(final GeneratedJDTAST<T> ast,
      final JDTASTLocation location, final ASTRewrite astRewrite) {

    final ASTNode dest = location.locate(ast.getRoot());
    final ASTNode src = this.src.locate(ast.getRoot());

    log.debug("src : {} -> {}", this.src.node.toString(), src.toString());
    log.debug("dest: {} -> {}", location.node.toString(), dest.toString());

    final ASTNode copiedSrc = ASTNode.copySubtree(astRewrite.getAST(), src);

    final ListRewrite listRewrite = astRewrite.getListRewrite(dest.getParent(),
        (ChildListPropertyDescriptor) dest.getLocationInParent());

    listRewrite.insertAfter(copiedSrc, dest, null);
    astRewrite.remove(src, null);
  }

}
