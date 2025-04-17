/*
 * Copyright (c) 2020-2025, Sreeni Viswanadha <sreeni@viswanadha.net>.
 * Copyright (c) 2024-2025, Marc Mazas <mazas.marc@gmail.com>.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the names of the copyright holders nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.javacc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.javacc.parser.Context;
import org.javacc.parser.JavaCCGlobals;
import org.javacc.parser.Options;

/** The {@link OutputFileDigest} class. */
abstract class OutputFileDigest {

  private static final String MD5_LINE_PART_1 = "/* JavaCC - OriginalChecksum=";
  private static final String MD5_LINE_PART_1q = "/\\* JavaCC - OriginalChecksum=";
  private static final String MD5_LINE_PART_2 = " (do not edit this line) */";
  private static final String MD5_LINE_PART_2q = " \\(do not edit this line\\) \\*/";

  private static final char[] HEX_DIGITS =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /** Constructs an instance of {@link OutputFileDigest}. */
  private OutputFileDigest() {}

  /**
   * Creates the Digest Line.
   *
   * @param digestStream
   */
  public static String getDigestLine(final DigestOutputStream digestStream) {
    return OutputFileDigest.MD5_LINE_PART_1
        + OutputFileDigest.toHexString(digestStream)
        + OutputFileDigest.MD5_LINE_PART_2;
  }

  /**
   * Create an MD5 {@link DigestOutputStream} for the provided {@link OutputStream}.
   *
   * @param stream
   * @throws NoSuchAlgorithmException
   */
  public static DigestOutputStream getDigestStream(final OutputStream stream)
      throws NoSuchAlgorithmException {
    return new DigestOutputStream(stream, MessageDigest.getInstance("MD5"));
  }

  /**
   * Check if the File already exists.
   *
   * @param file
   * @param toolName
   * @param compatibleVersion
   * @param options
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static boolean check(
      final File file,
      final String toolName,
      final String compatibleVersion,
      final List<String> options,
      final Context context)
      throws FileNotFoundException, IOException {
    // File does not exist
    if (!file.exists()) {
      System.out.println("File \"" + file.getName() + "\" does not exist.  Will create one.");
      return true;
    }

    // Generate the checksum of the file, and compare with any value stored in the file
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      String existingMD5 = null;
      final DigestOutputStream digestStream =
          OutputFileDigest.getDigestStream(new NullOutputStream());
      try (PrintWriter pw = new PrintWriter(digestStream)) {
        while ((line = reader.readLine()) != null) {
          if (line.startsWith(OutputFileDigest.MD5_LINE_PART_1)) {
            existingMD5 =
                line.replaceAll(OutputFileDigest.MD5_LINE_PART_1q, "")
                    .replaceAll(OutputFileDigest.MD5_LINE_PART_2q, "");
          } else {
            pw.println(line);
          }
        }
      }

      final String calculatedDigest = OutputFileDigest.toHexString(digestStream);
      if ((existingMD5 == null) || !existingMD5.equals(calculatedDigest)) {
        if (compatibleVersion != null) {
          OutputFileDigest.checkVersion(file, toolName, compatibleVersion, context);
        }

        if (!options.isEmpty()) {
          OutputFileDigest.checkOptions(file, options.toArray(new String[options.size()]), context);
        }

        // No checksum in file, or checksum differs
        return false;
      }
    } catch (final NoSuchAlgorithmException e) {
      throw (IOException) new IOException("No MD5 implementation").initCause(e);
    }

    // The file has not been altered since JavaCC created it. Rebuild it.
    System.out.println("File \"" + file.getName() + "\" is being rebuilt.");
    return true;
  }

  /**
   * Output a warning if the file was created with an incompatible version of JavaCC.
   *
   * @param file
   * @param toolName
   * @param versionId
   */
  private static void checkVersion(
      final File file, final String toolName, final String versionId, final Context context) {
    final String firstLine =
        "/* " + JavaCCGlobals.getIdString(toolName, file.getName()) + " Version ";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(firstLine)) {
          final String version = line.replaceFirst(".*Version ", "").replaceAll(" \\*/", "");
          //          if (!version.equals(versionId)) {
          if (!version.startsWith(versionId)) {
            context
                .errors()
                .warning(
                    file.getName()
                        + ": File is obsolete.  Please rename or delete this file so"
                        + " that a new one can be generated for you.");
            context
                .errors()
                .warning(
                    file.getName()
                        + " file version: "
                        + version
                        + ", javacc version: "
                        + versionId);
          }
          return;
        }
      }
      // If no version line is found, do not output the warning
    } catch (final FileNotFoundException e1) {
      // This should never happen
      context.errors().semantic_error("Could not open file " + file.getName() + " for writing.");
      throw new Error();
    } catch (final IOException e2) {
    }
  }

  /**
   * Read the options line from the file and compare to the options currently in use.<br>
   * Output a warning if they are different.
   *
   * @param file
   * @param options
   */
  private static void checkOptions(final File file, final String[] options, final Context context) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("/* JavaCCOptions: ")) {
          while (!line.endsWith(" */")) {
            line += EOL;
            line += reader.readLine();
          }
          final String currentOptions = Options.fmtOptionsArray(options);
          if (line.indexOf(currentOptions) == -1) {
            context
                .errors()
                .warning(
                    file.getName()
                        + ": Generated using incompatible options.  Please rename or delete this"
                        + " file so that a new one can be generated for you.");
          }
          return;
        }
      }
    } catch (final FileNotFoundException e1) {
      // This should never happen
      context.errors().semantic_error("Could not open file " + file.getName() + " for writing.");
      throw new Error();
    } catch (final IOException e2) {
    }
    // Not found so cannot check
  }

  static final String EOL = System.getProperty("line.separator");

  private static String toHexString(final DigestOutputStream digestStream) {
    final StringBuffer buffer = new StringBuffer(32);
    for (final byte b : digestStream.getMessageDigest().digest()) {
      buffer
          .append(OutputFileDigest.HEX_DIGITS[(b & 0xF0) >> 4])
          .append(OutputFileDigest.HEX_DIGITS[b & 0x0F]);
    }
    return buffer.toString();
  }

  /** The {@link NullOutputStream} implements an {@link OutputStream} to the null device. */
  private static class NullOutputStream extends OutputStream {

    @Override
    public void write(final byte[] arg0, final int arg1, final int arg2) throws IOException {}

    @Override
    public void write(final byte[] arg0) throws IOException {}

    @Override
    public void write(final int arg0) throws IOException {}
  }
}
