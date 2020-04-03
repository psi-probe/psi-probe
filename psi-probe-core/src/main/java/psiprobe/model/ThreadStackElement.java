/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model;

/**
 * The Class ThreadStackElement.
 */
public class ThreadStackElement {

  /** The class name. */
  private String className;

  /** The file name. */
  private String fileName;

  /** The method name. */
  private String methodName;

  /** The line number. */
  private int lineNumber;

  /** The native method. */
  private boolean nativeMethod;

  /**
   * Gets the class name.
   *
   * @return the class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Sets the class name.
   *
   * @param className the new class name
   */
  public void setClassName(String className) {
    this.className = className;
  }

  /**
   * Gets the file name.
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Sets the file name.
   *
   * @param fileName the new file name
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Gets the method name.
   *
   * @return the method name
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Sets the method name.
   *
   * @param methodName the new method name
   */
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  /**
   * Gets the line number.
   *
   * @return the line number
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Sets the line number.
   *
   * @param lineNumber the new line number
   */
  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * Checks if is native method.
   *
   * @return true, if is native method
   */
  public boolean isNativeMethod() {
    return nativeMethod;
  }

  /**
   * Sets the native method.
   *
   * @param nativeMethod the new native method
   */
  public void setNativeMethod(boolean nativeMethod) {
    this.nativeMethod = nativeMethod;
  }

}
