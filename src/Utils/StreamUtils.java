package Utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

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
		for(int readCount; (readCount = 
				readInputStreamWithTimeout(is, inputData, 6000)) > 0;) {// 6 second timeout
			if (abort) break;
			os.write(inputData, 0, readCount);
		}
	}

	public static void main(String args[]) throws IOException {
		Random rnd = new Random();
		int i = 0;
		while (true) {
			StringBuffer data = new StringBuffer();

			System.out.print(".");
			while (data.length() < 1000000000) {
				data.append(""+Math.abs(rnd.nextLong()));
			}
			InputStream is = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
			OutputStream os = new FileOutputStream(new File(args[0]+"temp" + (i++)));
			BufferedOutputStream bos = new BufferedOutputStream(os);
			StreamUtils.chain(is, bos);
			is.close();
			bos.close();
			os.close();
		}
	}
}
