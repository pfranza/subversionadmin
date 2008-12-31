package SigarUtils;

import org.hyperic.sigar.Sigar;

public class SigarFactory {

	private static final String BINLIB_PREFIX = System.getProperty("one-jar.dll.path", "");
	private static final String os = System.getProperty("os.name").toLowerCase();
	private static final String arch = System.getProperty("os.arch").toLowerCase();
	private static final String BINLIB_LINUX32_PREFIX = BINLIB_PREFIX + "sigar-x86-linux";
	private static final String BINLIB_LINUX64_PREFIX = BINLIB_PREFIX + "sigar-amd64-linux";
	private static final String BINLIB_MACOSX_PREFIX = BINLIB_PREFIX + "sigar-universal-macosx";
	private static final String BINLIB_WINDOWS32_PREFIX = BINLIB_PREFIX + "sigar-win32";
	private static final String BINLIB_WINDOWS64_PREFIX = BINLIB_PREFIX + "sigar-amd64-winnt";
	
	private static Sigar sigar = null;
	
	
	public static Sigar getInstance() {
		if(sigar == null) {
			
			String binlib = "";

			// Mac
			if (os.startsWith("mac os x")) {
				//TODO Need arch detection on mac
				binlib = BINLIB_MACOSX_PREFIX;
			} else if (os.startsWith("windows")) {			
				binlib = arch.equals("x86") ? BINLIB_WINDOWS32_PREFIX : BINLIB_WINDOWS64_PREFIX;
			} else {
				binlib = arch.equals("i386") ? BINLIB_LINUX32_PREFIX : BINLIB_LINUX64_PREFIX;
			}
			//TODO Need some work for solaris

			System.out.println("os: " + os);
			System.out.println("arch: " + arch);
			System.out.println("loading native code:  " + binlib);
			System.loadLibrary(binlib);

			sigar = new Sigar();


		}
		return sigar;
	}
	
}
