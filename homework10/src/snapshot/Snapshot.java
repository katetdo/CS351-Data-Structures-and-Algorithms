package snapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class Snapshot {
	private static final String PATH = "./logs/cs351Log";
	
	public static void capture(String... files) {
		try {
			writeRunHeader(new File(PATH));
		}
		catch (IOException ex) {
			System.out.println("Writing to " + PATH + "failed");
		}
		for(String file : files) {
			System.out.println("Copying " + file + " to " + PATH);
			try {
				copyFile(new File(file), new File(PATH));
			}
			catch (IOException ex) {
				System.out.println("Capture failed for " + file);
			}
		}
	}
	
	private static byte[] runHeader() {
		String result = "\n\n////////////////////////////////\nNew run at " + new Date(System.currentTimeMillis()) + "\n";
		return result.getBytes();
	}
	
	private static byte[] fileHeader(File file) {
		String result = "\n" +  file.getName() + "\n\n";
		return result.getBytes();
	}
	
	private static void writeRunHeader(File destFile) throws IOException{
		if(!destFile.exists())
			destFile.createNewFile();
		
		FileChannel destination = null;
		
		try {
			destination = new FileOutputStream(destFile, true).getChannel();
			destination.write(ByteBuffer.wrap(runHeader()));
		}
		finally {
			if(destination != null)
				destination.close();
		}
	}
	
	private static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists())
			destFile.createNewFile();
		
		FileChannel source = null, destination = null;
		
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile, true).getChannel();
			destination.write(ByteBuffer.wrap(fileHeader(sourceFile)));
			destination.transferFrom(source, destination.size(), source.size());
		}
		finally {
			if(source != null)
				source.close();
			if(destination != null)
				destination.close();
		}
	}
}
