package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class FileChoosingUtils {
	public static List<File> chooseFile(String key) {
		List<File> result = new ArrayList<File>();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String previousFilePath = ConfigurationUtil.get(key);
		if (null != previousFilePath) {
			chooser.setCurrentDirectory(new File(previousFilePath));
		}
		int chooseReturn = chooser.showOpenDialog(null);
		if (chooseReturn == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if(null != selectedFile) {
				result.add(selectedFile);
				if (key != null) {
					ConfigurationUtil.update(key, selectedFile.getAbsolutePath());
				}
			} else {
				File[] selectedFiles = chooser.getSelectedFiles();
				if (null != selectedFiles) {
					for (File f : selectedFiles) {
						result.add(f);
					}
					if(selectedFiles.length > 0 && key != null) {
						ConfigurationUtil.update(key, selectedFiles[0].getAbsolutePath());
					}
				}

			}
			// callback.process(selectedFile.getAbsolutePath());
		} else {
			System.out.println(String.format("[FileChoosingUtil]Selection canceled~"));
			return null;
		}
		return result;
	}
}
