package io.github.marcoheinisch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Class Logfile which creates and edit a simple logfile
 * @author Marco Heinisch
 *
 */
public class LogFile {
    private final String file;

    /**
     *
     * @param file File(name) in which the log will be saved.
     */
    public LogFile(String file, String headertext){
        this.file = file;
        createLogFile(headertext);
    }

    /**
     * Default constructor crates a "log.txt" file.
     */
    public LogFile(){
        this("log.txt", "> start log  - www  - router");
    }

    /**
     * Ad new line with content
     * @param text content to add
     */
    public void append(String text){
        try {
            Files.write(Paths.get(this.file), ("\n"+text).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param headertext
     */
    private void createLogFile(String headertext){
        File file = new File(this.file);
        try {
            file.createNewFile();
            append(headertext);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
