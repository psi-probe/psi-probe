/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating AsyncSocket objects.
 */
public final class AsyncSocketFactory {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AsyncSocketFactory.class);

  /**
   * Prevent Instantiation.
   */
  private AsyncSocketFactory() {
    // Prevent Instantiation
  }

  /**
   * Creates a new AsyncSocket object.
   *
   * @param server the server
   * @param port the port
   * @param timeout the timeout
   *
   * @return the socket
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Socket createSocket(String server, int port, long timeout) throws IOException {
    SocketWrapper socketWrapper = new SocketWrapper();
    socketWrapper.server = server;
    socketWrapper.port = port;

    Object sync = new Object();
    Thread socketThread = new Thread(new SocketRunnable(socketWrapper, sync));
    socketThread.setDaemon(true);
    Thread timeoutThread = new Thread(new TimeoutRunnable(sync, timeout * 1000));
    timeoutThread.setDaemon(true);

    timeoutThread.start();
    socketThread.start();

    synchronized (sync) {
      if (socketWrapper.socket == null) {
        try {
          sync.wait(timeout * 1000);
        } catch (InterruptedException e) {
          logger.trace("", e);
        }
      }
    }

    timeoutThread.interrupt();
    socketThread.interrupt();

    socketWrapper.valid = false;

    if (socketWrapper.socket == null && socketWrapper.exception != null) {
      throw socketWrapper.exception;
    }
    if (socketWrapper.socket == null) {
      throw new TimeoutException();
    }

    return socketWrapper.getSocket();
  }

  /**
   * The Class SocketWrapper.
   */
  static final class SocketWrapper {

    /** The socket. */
    Socket socket;

    /** The server. */
    String server;

    /** The port. */
    int port;

    /** The exception. */
    IOException exception;

    /** The valid. */
    boolean valid = true;

    /**
     * Gets the socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
      return socket;
    }

    /**
     * Sets the socket.
     *
     * @param socket the new socket
     */
    public void setSocket(Socket socket) {
      this.socket = socket;
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public String getServer() {
      return server;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
      return port;
    }

    /**
     * Sets the exception.
     *
     * @param exception the new exception
     */
    public void setException(IOException exception) {
      this.exception = exception;
    }

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
      return valid;
    }

    /**
     * Instantiates a new socket wrapper.
     */
    private SocketWrapper() {}

  }

  /**
   * The Class SocketRunnable.
   */
  static final class SocketRunnable implements Runnable {

    /** The socket wrapper. */
    private final SocketWrapper socketWrapper;

    /** The sync. */
    private final Object sync;

    /**
     * Instantiates a new socket runnable.
     *
     * @param socketWrapper the socket wrapper
     * @param sync the sync
     */
    private SocketRunnable(SocketWrapper socketWrapper, Object sync) {
      this.socketWrapper = socketWrapper;
      this.sync = sync;
    }

    @Override
    public void run() {
      try (Socket socket = new Socket(socketWrapper.getServer(), socketWrapper.getPort())) {
        socketWrapper.setSocket(socket);
        if (!socketWrapper.isValid()) {
          socketWrapper.getSocket().close();
          socketWrapper.setSocket(null);
        }
      } catch (IOException e) {
        logger.trace("", e);
        socketWrapper.setException(e);
      }
      synchronized (sync) {
        sync.notify();
      }
    }

  }

  /**
   * The Class TimeoutRunnable.
   */
  static final class TimeoutRunnable implements Runnable {

    /** The sync. */
    private final Object sync;

    /** The timeout. */
    private final long timeout;

    /**
     * Instantiates a new timeout runnable.
     *
     * @param sync the sync
     * @param timeout the timeout
     */
    private TimeoutRunnable(Object sync, long timeout) {
      this.sync = sync;
      this.timeout = timeout;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(timeout);
        synchronized (sync) {
          sync.notify();
        }
      } catch (InterruptedException e) {
        logger.trace("", e);
      }
    }

  }

}
