package org.appproductions.fileParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.UtilsResources;

public class FileParser {

	protected static List<String[]> readLines(String file, int ignoreLines) {
		List<String[]> result = new ArrayList<String[]>();
		try {
			InputStream in=UtilsResources.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			for (int i = 0; i < ignoreLines; i++)
				reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] components = line.split(";");
				result.add(components);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected static String getComponentString(String[] components, int index) {
		if (components.length > index) {
			return components[index];
		}
		return "";
	}

	protected static int getComponentInt(String[] components, int index) {
		if (components.length > index) {
			try {
				return Integer.parseInt(components[index]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

	protected static float getComponentFloat(String[] components, int index) {
		if (components.length > index) {
			try {
				return Float.parseFloat(components[index]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 0f;
			}
		}
		return 0f;
	}

	protected static boolean getComponentBool(String[] components, int index) {
		if (components.length > index) {
			return getComponentInt(components,index) == 1;
		}
		return false;
	}

}
