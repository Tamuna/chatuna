package ge.edu.freeuni.chatuna;

import java.io.IOException;
import java.io.OutputStream;

public class SendReceiveHelper extends Thread {

    private OutputStream out;
    private byte[] bytes;

    public SendReceiveHelper(OutputStream out, byte[] bytes) {
        this.out = out;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        try {
            out.write(this.bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
