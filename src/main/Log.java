package main;

public class Log {
	static final boolean enabled = true;
	
	public static void log(String text) {
		if (enabled) {
			System.out.println(text);
		}
	}
}
