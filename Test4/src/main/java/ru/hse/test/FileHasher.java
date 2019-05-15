package ru.hse.test;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


/**
 * FileHasher a console application that calculates the check sum of the file system directory according to this rule:
 * f(file) = MD5(<содержимое>)
 * f(dir) = MD5(<имя папки> + f(file1) + ...)
 */
public class FileHasher {

    private final static int BUFFER_SIZE = 1024;


    /**
     * Pool of threads for multithread version
     */
    private final static ForkJoinPool pool = new ForkJoinPool();

    /**
     * Calculates the hash for given file/directory using algorithm md5 and only one thread
     * @param file file to get hash of
     * @return hash for given file in format of sequence of bytes
     * @throws IOException if an arrow occur while opening or reading
     * @throws NoSuchAlgorithmException if ann arrow occur in getting md5 algorithm
     */
    public static byte[] singleThreadHasher(@NotNull File file) throws IOException, NoSuchAlgorithmException {
        final var messageDigest = MessageDigest.getInstance("MD5");
        return hash(file, messageDigest);
    }

    /**
     * Calculates the hash for given file/directory using algorithm md5 and many threads
     * @param file file to get hash of
     * @return hash for given file in format of sequence of bytes
     * @throws NoSuchAlgorithmException
     */
    public static byte[] multiThreadHasher(@NotNull File file) throws NoSuchAlgorithmException {
        final var messageDigest = MessageDigest.getInstance("MD5");
        return pool.invoke(new HashTask(file, messageDigest));
    }

    private static byte[] hash(File file, MessageDigest messageDigest) throws IOException, NoSuchAlgorithmException {
        if (file.isDirectory()) {
            hashDirectory(file, messageDigest);
        }
        if (file.isFile()) {
            hashFile(file, messageDigest);
        }
        //returns the message digest associated with this stream and compete computation
        return messageDigest.digest();
    }

    private static byte[] hashFile(@NotNull File file, MessageDigest messageDigest) throws IOException {
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), messageDigest)) {
            var byteArray = new byte[BUFFER_SIZE];
            while (dis.read(byteArray) != -1) ; //empty loop to clear the data
        }
        return messageDigest.digest();
    }

    private static void hashDirectory(@NotNull File directory, MessageDigest messageDigest) throws IOException, NoSuchAlgorithmException {
        messageDigest.update(directory.getName().getBytes());
        var entries = directory.listFiles(); //list of inner files
        if (entries != null) {
            for (var entry : entries) {
                if (entry.isFile() || entry.isDirectory()) {
                    messageDigest.update(hash(entry, messageDigest));
                }
            }
        }
    }

    /**
     * Task for thread, in which calculates a hash for one file/directory
     */
    private static class HashTask extends RecursiveTask<byte[]> {
        /**
         * file to hash
         */
        private final File file;

        /**
         * hashing algorithm
         */
        MessageDigest messageDigest;

        public HashTask(@NotNull File file, MessageDigest messageDigest) {
            this.file = file;
            this.messageDigest = messageDigest;
        }

        @Override
        protected byte[] compute() {
            if (file.isDirectory()) {
                messageDigest.update(file.getName().getBytes());
                var entries = file.listFiles();
                final var tasks = new ArrayList<HashTask>();

                if (entries != null) {
                    for (var entry : entries) {
                        if (entry.isFile() || entry.isDirectory()) {
                            HashTask task = new HashTask(entry, messageDigest);
                            tasks.add(task);
                            task.fork();
                        }
                    }
                }

                for (var task : tasks) {
                    messageDigest.update(task.join());
                }
            }

            if (file.isFile()) {
                try {
                    messageDigest.update(hashFile(file, messageDigest));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return messageDigest.digest();
        }
    }
}
