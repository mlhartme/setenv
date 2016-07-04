package net.oneandone.setenv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Setenv implements Runnable {
    private static Setenv lazySetenv;

    public static Setenv get() {
        if (lazySetenv == null) {
            lazySetenv = create();
        }
        return lazySetenv;
    }

    public static Setenv create() {
        String str;
        Setenv result;

        str = System.getenv("SETENV_BASE");
        result = new Setenv(new PrintWriter(System.out, true), str == null ? null : Paths.get(str + "-" + pid()));
        Runtime.getRuntime().addShutdownHook(new Thread(result));
        return result;
    }

    public static int pid() {
        String str;
        int idx;

        str = ManagementFactory.getRuntimeMXBean().getName();
        idx = str.indexOf('@');
        if (idx != -1) {
            str = str.substring(0, idx);
        }
        return Integer.parseInt(str);
    }

    //--

    private PrintWriter messages;
    private final Path dest;
    private String cd;
    private Map<String, String> map;

    public Setenv(PrintWriter messages, Path dest) {
        this.messages = messages;
        this.dest = dest;
        this.cd = null;
        this.map = new LinkedHashMap<>();
    }

    public boolean isConfigured() {
        return dest != null;
    }

    public String setenvBash() {
        byte[] buffer = new byte[2];
        InputStream src;
        ByteArrayOutputStream dest;
        int count;

        src = getClass().getResourceAsStream("/setenv.bash");
        dest = new ByteArrayOutputStream();
        while (true) {
            try {
                count = src.read(buffer);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            if (count == -1) {
                try {
                    return new String(dest.toByteArray(), "UTF8");
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
            dest.write(buffer, 0, count);
        }
    }

    public void cd(String path) {
        this.cd = path;
    }

    public void set(String key, String value) {
        map.put(key, value);
    }

    @Override
    public void run() {
        if (dest == null) {
            messages.print(toString());
        } else {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace(messages);
            }
        }
    }

    public void save() throws IOException {
        Files.write(dest, toString().getBytes("UTF8"));
    }

    public String toString() {
        StringBuilder result;

        result = new StringBuilder();
        if (cd != null) {
            result.append("cd " + cd + "\n");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                result.append("export " + entry.getKey() + "='" + entry.getValue() + "'\n");
            }
        }
        return result.toString();
    }
}