package wind.model.players.saving;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import wind.Config;

/**
 * Creates a ZIP archive of the character directory
 * 
 * @author Octave
 * @date FEB 2 2013
 */
public class CharacterBackup {
	/**
	 * Character file directory
	 */
	private static final String INPUT_DIRECTORY = Config.DATA_LOC + "characters";

	/**
	 * Archive directory
	 */
	private static final String OUTPUT_DIRECTORY = Config.DATA_LOC + "backups/characters/";

	/**
	 * Starts the backup procedure
	 */
	public static void startBackupService() {
		final long startTime = System.currentTimeMillis();
		System.out.println("Starting backup service . . .");
		System.out.println("\tInput directory: "
				+ CharacterBackup.INPUT_DIRECTORY);
		System.out.println("\tOutput directory: "
				+ CharacterBackup.OUTPUT_DIRECTORY);
		try {
			CharacterBackup.zipFolder(CharacterBackup.INPUT_DIRECTORY,
					CharacterBackup.OUTPUT_DIRECTORY + "/Character Archive ["
							+ CharacterBackup.getTime() + "].zip");
			System.out.println("\tSuccessfully archived "
					+ CharacterBackup.total + " files");
		} catch (final Exception e) {
			System.out.println("\tAn error has occured: " + e);
		}
		final long endTime = System.currentTimeMillis();
		final long duration = endTime - startTime;
		System.out.println("\tProcedure took: " + duration + "ms");
		System.out.println();
		CharacterBackup.total = 0;
	}

	/**
	 * Formats the time and date for use in filename
	 * 
	 * @return Formatted time
	 */
	private static String getTime() {
		final Date getDate = new Date();
		final String timeFormat = "M\u2215d\u2215yy h\u02D0mma";
		final SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.format(getDate);
	}

	/**
	 * Prepares the folder to be archived
	 * 
	 * @param srcFolder
	 *            - Source folder to ZIP
	 * @param destZipFile
	 *            - Destination ZIP archive
	 */
	private static void zipFolder(final String srcFolder,
			final String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		CharacterBackup.addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	/**
	 * Adds file to ZIP archive
	 * 
	 * @param path
	 *            - File directory
	 * @param srcFolder
	 *            - Source folder
	 * @param ZipOutputSream
	 *            - ZIP archive
	 */
	private static void addFileToZip(final String path, final String srcFile,
			final ZipOutputStream zip) throws Exception {

		final File folder = new File(srcFile);
		if (folder.isDirectory()) {
			CharacterBackup.addFolderToZip(path, srcFile, zip);
		} else {
			final byte[] buf = new byte[1024];
			int len;
			final FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
			in.close();
		}
		CharacterBackup.total++;
	}

	/**
	 * Adds folder to ZIP archive
	 * 
	 * @param path
	 *            - File directory
	 * @param srcFolder
	 *            - Source folder
	 * @param ZipOutputSream
	 *            - ZIP archive
	 */
	private static void addFolderToZip(final String path,
			final String srcFolder, final ZipOutputStream zip) throws Exception {
		final File folder = new File(srcFolder);

		for (final String fileName : folder.list()) {
			if (path.equals("")) {
				CharacterBackup.addFileToZip(folder.getName(), srcFolder + "/"
						+ fileName, zip);
			} else {
				CharacterBackup.addFileToZip(path + "/" + folder.getName(),
						srcFolder + "/" + fileName, zip);
			}
		}
	}

	public static int total = 0;
}