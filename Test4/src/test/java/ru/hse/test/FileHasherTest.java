package ru.hse.test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import static java.lang.System.clearProperty;
import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

class FileHasherTest {

    private final static int BUFFER_SIZE = 1024;

    @Test
    void singleMultiThreadHasherEqualAnswersTest() throws IOException, NoSuchAlgorithmException {
        var file = Main.createFileTree();
        byte[] multiThreadAnswer = FileHasher.multiThreadHasher(file);
        byte[] singleThreadAnswer = FileHasher.multiThreadHasher(file);
        assertArrayEquals(singleThreadAnswer, multiThreadAnswer);
    }

    @Test
    void singleThreadHasherDoubleCallingTest() throws IOException, NoSuchAlgorithmException {
        var file = Main.createFileTree();
        byte[] singleThreadAnswer1 = FileHasher.singleThreadHasher(file);
        byte[] singleThreadAnswer2 = FileHasher.singleThreadHasher(file);
        assertArrayEquals(singleThreadAnswer1, singleThreadAnswer2);
    }

    @Test
    void multiThreadHasherDoubleCallingTest() throws IOException, NoSuchAlgorithmException {
        var file = Main.createFileTree();
        byte[] multiThreadAnswer1 = FileHasher.singleThreadHasher(file);
        byte[] multiThreadAnswer2 = FileHasher.singleThreadHasher(file);
        assertArrayEquals(multiThreadAnswer1, multiThreadAnswer2);
    }
}