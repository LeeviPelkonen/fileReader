import java.beans.ExceptionListener;
import java.io.FileInputStream;

public class CustomReader implements Runnable {
    private FileInputStream inputStream;
    private int bufferSize;
    private ReaderListener readerListener;
    private ExceptionListener exceptionListener;

    public CustomReader(FileInputStream inputStream, int bufferSize, ReaderListener readerListener, ExceptionListener exceptionListener) {
        this.inputStream = inputStream;
        this.bufferSize = bufferSize;
        this.readerListener = readerListener;
        this.exceptionListener = exceptionListener;
    }

    public void run() {
        int readIndex = 0;
        byte[] buffer = new byte[bufferSize];

        try {
            int byteData;
            //loop untill we hit the bufferSize
            while (bufferSize > readIndex) {
                byteData = inputStream.read();
                //no more data so we can return buffer and stop reading
                if (byteData == -1) {
                    readerListener.afterRead(buffer, true);
                    return;
                }         
                buffer[readIndex] = (byte)byteData;
                readIndex++;
            }
            readerListener.afterRead(buffer, false);
        } catch (Exception e) {
            exceptionListener.exceptionThrown(e);
        }
    }
}