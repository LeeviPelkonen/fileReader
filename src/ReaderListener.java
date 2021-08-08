public interface ReaderListener {
    void afterRead(byte[] byteData, boolean fileEndReached);
}
