
package org.javacc.jjtree;

import org.javacc.parser.Context;
import org.javacc.parser.Options;


public class JJTreeContext extends Context {

  private final JJTreeOptions treeOptions = new JJTreeOptions();

  public JJTreeContext() {
    Options.resOptions.put("MULTI", Boolean.FALSE);
    Options.resOptions.put("NODE_DEFAULT_VOID", Boolean.FALSE);
    Options.resOptions.put("NODE_SCOPE_HOOK", Boolean.FALSE);
    Options.resOptions.put("NODE_USES_PARSER", Boolean.FALSE);
    Options.resOptions.put("BUILD_NODE_FILES", Boolean.TRUE);
    Options.resOptions.put("VISITOR", Boolean.FALSE);
    Options.resOptions.put("VISITOR_METHOD_NAME_INCLUDES_TYPE_NAME", Boolean.FALSE);
    Options.resOptions.put("TRACK_TOKENS", Boolean.FALSE);

    Options.resOptions.put("NODE_PREFIX", "AST");
    Options.resOptions.put("NODE_PACKAGE", "");
    Options.resOptions.put("NODE_EXTENDS", "");
    Options.resOptions.put("NODE_CLASS", "");
    Options.resOptions.put("NODE_FACTORY", "");
    Options.resOptions.put("NODE_INCLUDES", "");
    Options.resOptions.put("OUTPUT_FILE", "");
    Options.resOptions.put("VISITOR_DATA_TYPE", "");
    Options.resOptions.put("VISITOR_RETURN_TYPE", "Object");
    Options.resOptions.put("VISITOR_EXCEPTION", "");

    Options.resOptions.put("NODE_DIRECTORY", "");
    Options.resOptions.put("JJTREE_OUTPUT_DIRECTORY", "");


    // TODO :: 2013/07/23 -- This appears to be a duplicate from the parent
    // class
    Options.resOptions.put(Options.UO__JDK_VERSION, "1.5");

    // Also appears to be a duplicate
    Options.resOptions.put(Options.UO__NAMESPACE, "");

    // Also appears to be a duplicate
    Options.resOptions.put(Options.UO__IGNORE_ACTIONS, Boolean.FALSE);
  }

  public final JJTreeOptions treeOptions() {
    return treeOptions;
  }

  /**
   * Check options for consistency
   */
  final void validate() {
    if (!treeOptions().getVisitor()) {
      if (treeOptions().getVisitorDataType().length() > 0) {
        errors().warning("VISITOR_DATA_TYPE option will be ignored since VISITOR is false");
      }
      if ((treeOptions().getVisitorReturnType().length() > 0)
          && !treeOptions().getVisitorReturnType().equals("Object")) {
        errors().warning("VISITOR_RETURN_TYPE option will be ignored since VISITOR is false");
      }
      if (treeOptions().getVisitorException().length() > 0) {
        errors().warning("VISITOR_EXCEPTION option will be ignored since VISITOR is false");
      }
    }
  }
}
