package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class FileChoosingUtils {
	private static final String configKey = "fileLoader";

	public static final int SAVE_DIALOG = 1;
	public static final int OPEN_DIALOG = 2;
	public static final int OPEN_DIALOG_MUlTIPLE = 3;

	public static List<File> chooseFile(String key, int openOption) {
		List<File> result = new ArrayList<File>();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (openOption == OPEN_DIALOG_MUlTIPLE)
			chooser.setMultiSelectionEnabled(true);
		else
			chooser.setMultiSelectionEnabled(false);
		String previousFilePath = ConfigurationUtil.get(configKey, key);
		if (null != previousFilePath) {
			chooser.setCurrentDirectory(new File(previousFilePath));
		}
		int chooseReturn = -1;
		if (openOption == SAVE_DIALOG)
			chooseReturn = chooser.showSaveDialog(null);
		else if (openOption == OPEN_DIALOG || openOption == OPEN_DIALOG_MUlTIPLE)
			chooseReturn = chooser.showOpenDialog(null);
		if (chooseReturn == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = chooser.getSelectedFiles();
			if (null != selectedFiles && selectedFiles.length > 0) {
				for (File f : selectedFiles) {
					result.add(f);
				}
				if (selectedFiles.length > 0 && key != null) {
					ConfigurationUtil.update(configKey, key,
							selectedFiles[0].getAbsolutePath());
				}
			} else {
				File selectedFile = chooser.getSelectedFile();
				if (null != selectedFile) {
					result.add(selectedFile);
					if (key != null) {
						ConfigurationUtil.update(configKey, key,
								selectedFile.getAbsolutePath());
					}
				}
			}
		} else {
			System.out.println(String.format("[FileChoosingUtil]Selection canceled~"));
			return null;
		}
		return result;
	}
}
