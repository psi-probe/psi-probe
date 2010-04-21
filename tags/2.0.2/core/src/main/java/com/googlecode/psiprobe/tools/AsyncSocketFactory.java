/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools;

import java.io.IOException;
import java.net.Socket;

public class AsyncSocketFactory {

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
                    //
                }
            }
        }

        timeoutThread.interrupt();
        socketThread.interrupt();

        socketWrapper.valid = false;

        if (socketWrapper.getSocket() == null && socketWrapper.exception != null) {
            throw socketWrapper.exception;
        } else if (socketWrapper.getSocket() == null) {
            throw new TimeoutException();
        }

        return socketWrapper.getSocket();
    }

    static class SocketWrapper {
        private Socket socket = null;
        private String server;
        private int port;
        private IOException exception;
        private boolean valid = true;

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        public String getServer() {
            return server;
        }

        public int getPort() {
            return port;
        }

        public void setException(IOException exception) {
            this.exception = exception;
        }

        public boolean isValid() {
            return valid;
        }
    }

    static class SocketRunnable implements Runnable {
        private SocketWrapper socketWrapper;
        private final Object sync;


        public SocketRunnable(SocketWrapper socketWrapper, Object sync) {
            this.socketWrapper = socketWrapper;
            this.sync = sync;
        }

        public void run() {
            try {
                socketWrapper.setSocket(new Socket(socketWrapper.getServer(), socketWrapper.getPort()));
                if (!socketWrapper.isValid()) {
                    socketWrapper.getSocket().close();
                    socketWrapper.setSocket(null);
                }
            } catch (IOException e) {
                socketWrapper.setException(e);
            }
            synchronized (sync) {
                sync.notify();
            }
        }
    }

    static class TimeoutRunnable implements Runnable {
        private final Object sync;
        private long timeout;

        public TimeoutRunnable(Object sync, long timeout) {
            this.sync = sync;
            this.timeout = timeout;
        }

        public void run() {
            try {
                Thread.sleep(timeout);
                synchronized (sync) {
                    sync.notify();
                }
            } catch (InterruptedException e) {
                //
            }
        }

    }
}
