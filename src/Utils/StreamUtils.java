package Utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static boolean abort;
	
	public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
			throws IOException  {
		int bufferOffset = 0;
		long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
			int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
			// can alternatively use bufferedReader, guarded by isReady():
			int readResult = is.read(b, bufferOffset, readLength);
			if (readResult == -1) break;
			bufferOffset += readResult;
		}
		return bufferOffset;
	}

	static void chain(InputStream is, BufferedOutputStream os) throws IOException {
		byte[] inputData = new byte[1024];
		abort = false;
		for(int readCount; (readCount = readInputStreamWithTimeout(is, inputData, 6000)) > 0;) {
			if (abort) break;
			os.write(inputData, 0, readCount);// 6 second timeout
		}
	}
}
