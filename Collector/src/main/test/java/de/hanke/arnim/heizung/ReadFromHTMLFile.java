package de.hanke.arnim.heizung;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFromHTMLFile {

    public static String getContentFromHTMLFile(String fileName) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(ReadFromHTMLFile.class.getClassLoader().getResource("ISG/" + fileName).getPath().replace("%20", " ").replaceFirst("/", "")));
        return new String(encoded);
    }
}
