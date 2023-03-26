package Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StreamUtilsTest {

	@Test
	public void test1() throws IOException {
		Random rnd = new Random(0);
		StringBuffer data = new StringBuffer();
		System.out.println("before data build");
		while (data.length() < 1000000) {
			data.append(""+Math.abs(rnd.nextLong()));
			System.out.println(data.length());
		}
		InputStream is = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamUtils.chain(is, os);
		System.out.println("before assert");
		Assertions.assertEquals(data.toString(),os.toString("UTF-8"));
	}
}
