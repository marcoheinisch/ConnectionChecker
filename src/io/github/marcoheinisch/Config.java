package io.github.marcoheinisch;

import java.io.FileReader;
import java.util.Properties;

public class Config {
    private Properties p;

    public Config() {
        p = new Properties();
        try {
            FileReader fr = new FileReader("config.txt");
            p.load(fr);
            fr.close();
        } catch (Exception e) {
            System.out.println("> Failed to find or read the config.txt!");
            e.printStackTrace();
            return;
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public String get(String key) {
        return (String) p.get(key);
    }
}
