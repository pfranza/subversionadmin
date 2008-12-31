package SigarUtils;

import org.hyperic.sigar.Sigar;

public class SigarFactory {

	private static final String BINLIB_PREFIX = System.getProperty("one-jar.dll.path", "");
	private static final String os = System.getProperty("os.name").toLowerCase();
	private static final String arch = System.getProperty("os.arch").toLowerCase();
	private static final String BINLIB_LINUX32_PREFIX = BINLIB_PREFIX + "linux32/";
	private static final String BINLIB_LINUX64_PREFIX = BINLIB_PREFIX + "linux64/";
	private static final String BINLIB_MACOSX_PREFIX = BINLIB_PREFIX + "macosx/";
	private static final String BINLIB_WINDOWS32_PREFIX = BINLIB_PREFIX + "windows32/";
	private static final String BINLIB_WINDOWS64_PREFIX = BINLIB_PREFIX + "windows64/";
	
	private static Sigar sigar = null;
	
	
	public static Sigar getInstance() {
		if(sigar == null) {
			
			String binlib = "";

			// Mac
			if (os.startsWith("mac os x")) {
				//TODO Need arch detection on mac
				binlib = BINLIB_MACOSX_PREFIX;
			// Windows
			} else if (os.startsWith("windows")) {
				if (arch.equals("x86")) {
					binlib = BINLIB_WINDOWS32_PREFIX;
				} else {
					binlib = BINLIB_WINDOWS64_PREFIX;
				}
			// So it have to be Linux
			} else {
				if (arch.equals("i386")) {
					binlib = BINLIB_LINUX32_PREFIX;
				} else {
					binlib = BINLIB_LINUX64_PREFIX;
				}
			}
			//TODO Need some work for solaris

			
			System.out.println("Test: loading native code:  " + binlib);
			System.loadLibrary(binlib + "sigar");

			sigar = new Sigar();

		}
		return sigar;
	}
	
}
