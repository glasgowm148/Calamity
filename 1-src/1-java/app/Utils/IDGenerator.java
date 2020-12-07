package Utils;

import java.util.Random;
import java.util.UUID;

public class IDGenerator {
	private static Random random = new Random(1);
	public static long getID() {
		long id;
		byte[] array = new byte[16];
		random.nextBytes(array);
		id = UUID.nameUUIDFromBytes( array ).getLeastSignificantBits();
		return id;
	}
}
