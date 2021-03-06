/*
 * The MIT License
 *
 * Copyright 2012 bfeng5.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mytorrent.p2p;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author bfeng5
 */
public class P2PReceiver extends Thread {

    private String filename;
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    public P2PReceiver(Socket socket, String filename) throws IOException {
        this.socket = socket;
        this.filename = filename;

        this.os = socket.getOutputStream();
        this.is = socket.getInputStream();

        os.write(this.filename.getBytes());
        os.flush();
        socket.shutdownOutput();
    }

    @Override
    public void run() {
        try {
            FileUtils.copyInputStreamToFile(is, new File("received/" + filename));
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(P2PReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
