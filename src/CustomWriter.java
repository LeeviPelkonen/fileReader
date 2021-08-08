import java.beans.ExceptionListener;
import java.io.FileOutputStream;

public class CustomWriter implements Runnable {
    private FileOutputStream outputStream;
    private byte[] buffer;
    private WriterListener writerListener;
    private ExceptionListener exceptionListener;

    public CustomWriter(FileOutputStream outputStream, byte[] buffer, WriterListener writerListener, ExceptionListener exceptionListener) {
                this.outputStream = outputStream;
                this.buffer = buffer;
                this.writerListener = writerListener;
                this.exceptionListener = exceptionListener;
    }

    public void run() {
        int writeIndex = 0;

        try {
            int byteData;
            //loop untill we hit the buffers length
            while (buffer.length > writeIndex) {
                byteData = buffer[writeIndex];
                //if buffer hits empty byte stop reading
                if (byteData == 0) {
                    return;
                }
                outputStream.write(byteData);      
                writeIndex++;
            }
        } catch (Exception e) {
            exceptionListener.exceptionThrown(e);
        }
        writerListener.afterWrite();
    }
}