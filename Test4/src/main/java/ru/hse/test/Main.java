package ru.hse.test;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class Main {

    private final static int BUFFER_SIZE = 1024;

    private static void fillRandomFile(@NotNull File file) throws IOException {
        Random rand = new Random(currentTimeMillis());
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileOutputStream fos = new FileOutputStream(file.getName())) {
            for (int i = 0; i < 300; ++i) {
                rand.nextBytes(buffer);
                fos.write(buffer);
            }
        }
    }

    public static File createFileTree() throws IOException {
        File dir = Files.createTempDirectory("MainDirectory").toFile();
        for (int i = 0; i < 30; i++) {
            File file = Files.createTempFile(dir.toPath(), "file" , String.valueOf(i)).toFile();
            fillRandomFile(file);
        }
        return dir;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        File directory = createFileTree();
        long startTime = currentTimeMillis();
        FileHasher.singleThreadHasher(directory);
        long singleThreadTime = currentTimeMillis() - startTime;
        System.out.println("single thread hasher works " + singleThreadTime + " ms");
        startTime = currentTimeMillis();
        FileHasher.multiThreadHasher(directory);
        long multiThreadTime = currentTimeMillis() - startTime;
        System.out.println("multi thread hasher works " + multiThreadTime + " ms");
        System.out.println("single thread hasher works faster then single thread hasher in " + multiThreadTime / singleThreadTime + " times");
    }
}
