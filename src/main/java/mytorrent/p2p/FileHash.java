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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bo Feng
 * @version 1.0
 */
public class FileHash {

    public class Entry {

        private long peerId;
        private String filename;

        public Entry() {
        }

        public Entry(long peerId, String filename) {
            this.peerId = peerId;
            this.filename = filename;
        }

        public long getPeerId() {
            return peerId;
        }

        public void setPeerId(long peerId) {
            this.peerId = peerId;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        @Override
        public String toString() {
            return String.format("%s:%s", peerId, filename);
        }
    }
    private Map<String, Entry> hash;

    public FileHash() {
        this.hash = new HashMap<String, Entry>();
    }

    public synchronized void addEntry(Entry entry) {
        String key = entry.toString();
        this.hash.put(key, entry);
    }

    public synchronized Entry getEntry(int peerId, String filename) {
        return this.hash.get(String.format("%s:%s", peerId, filename));
    }

    /**
     *
     * @return all entries maintained in the hash map
     */
    public synchronized Entry[] search() {
        return toArray(this.hash.values());
    }

    /**
     * Find out all entries that have the peerId
     *
     * @param peerId
     * @return
     */
    public synchronized Entry[] search(int peerId) {
        ArrayList<Entry> result = new ArrayList<Entry>();

        for (String key : this.hash.keySet()) {
            String peerIdString = key.split(":", 2)[0];
            if (peerIdString.equals(String.valueOf(peerId))) {
                result.add(this.hash.get(key));
            }
        }

        return toArray(result);
    }

    /**
     * Find out all entries that have the filename. It also supports partial
     * sequence matching.
     *
     * @param filename
     * @return
     */
    public synchronized Entry[] search(String filename) {
        ArrayList<Entry> result = new ArrayList<Entry>();

        for (String key : this.hash.keySet()) {
            String filenameString = key.split(":", 2)[1];
            if (filenameString.contains(filename)) {
                result.add(this.hash.get(key));
            }
        }

        return toArray(result);
    }
    //#
    //Remove all entry with respect to a specific peerId.
    //This method is mainly used in IndexServer REG static updating as is:
    //       this.hash.remove(peerId);
    //input peerId
    //return void
    public synchronized void removeall(long peerId) {
 
        for (String key : this.hash.keySet()) {
            String peerIdString = key.split(":", 2)[0];
            if (peerIdString.equals(String.valueOf(peerId))) {
                this.hash.remove(key);
            }
        }
    }

    private Entry[] toArray(Collection<Entry> result) {
        return (Entry[]) result.toArray(new Entry[result.size()]);
    }
}
