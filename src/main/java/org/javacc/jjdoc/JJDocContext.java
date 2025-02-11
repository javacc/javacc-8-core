
package org.javacc.jjdoc;

import org.javacc.parser.Context;
import org.javacc.parser.Options;


public class JJDocContext extends Context {

  public JJDocContext() {
    super(new Options());

    Options.resOptions.put("ONE_TABLE", Boolean.TRUE);
    Options.resOptions.put("TEXT", Boolean.FALSE);
    Options.resOptions.put("XTEXT", Boolean.FALSE);
    Options.resOptions.put("BNF", Boolean.FALSE);
    Options.resOptions.put("JCC", Boolean.FALSE);
    Options.resOptions.put("OUTPUT_FILE", "");
    Options.resOptions.put("CSS", "");
  }

  /**
   * Find the one table value.
   */
  public final boolean getOneTable() {
    return Options.booleanValue("ONE_TABLE");
  }

  /**
   * Find the CSS value.
   */
  public final String getCSS() {
    return Options.stringValue("CSS");
  }

  /**
   * Find the text value.
   */
  public final boolean getText() {
    return Options.booleanValue("TEXT");
  }

  public final boolean getXText() {
    return Options.booleanValue("XTEXT");
  }

  /**
   * Find the BNF value.
   */
  public final boolean getBNF() {
    return Options.booleanValue("BNF");
  }

  /**
   * Find the BNF value.
   */
  public final boolean getJCC() {
    return Options.booleanValue("JCC");
  }

  /**
   * Find the output file value.
   */
  public final String getOutputFile() {
    return Options.stringValue("OUTPUT_FILE");
  }
}
