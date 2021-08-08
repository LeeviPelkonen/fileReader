import java.beans.ExceptionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class App {
    static int bufferSize = 25;
    static String inputFilePath = ".\\src\\input.txt";
    static String outputFilePath = ".\\src\\output.txt";
    static FileInputStream inputStream;
    static FileOutputStream outputStream;
    static CustomReader reader;
    static boolean exit;

    public static void main(String[] args) throws Exception {
        exit = false;
        try {
            inputStream = new FileInputStream(inputFilePath);
            outputStream = new FileOutputStream(outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //if bufferSize is 0 or negative stop
        if (bufferSize < 1) {
            return;
        }
        //creating Custom Reader and start Reading
        reader = new CustomReader(inputStream, bufferSize, readerListener, exceptionListener);
        Thread readerThread = new Thread(reader);
        readerThread.start();
    }

    private static ReaderListener readerListener = new ReaderListener() {
        @Override
        public void afterRead(byte[] buffer, boolean fileEndReached) {
            exit = fileEndReached;
            //create custom Writer and start Writing
            Thread writerThread = new Thread(new CustomWriter(outputStream, buffer, writerListener, exceptionListener));
            writerThread.start();
        };
    };

    private static WriterListener writerListener = new WriterListener() {
        @Override
        public void afterWrite() {
            //when we are done stop reading and close streams
            if (exit) {
                closeStreams();
                return;
            }
            //Continue Reading
            Thread readerThread = new Thread(reader);
            readerThread.start();
        };
    };

    private static ExceptionListener exceptionListener = new ExceptionListener() {
        @Override
        public void exceptionThrown(Exception e) {
            e.printStackTrace();
        };
    };

    private static void closeStreams() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
