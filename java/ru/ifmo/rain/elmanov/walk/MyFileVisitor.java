package ru.ifmo.rain.elmanov.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class MyFileVisitor extends SimpleFileVisitor<Path> {
    String pathName;
    BufferedWriter writer;

    public MyFileVisitor(String pathName, BufferedWriter writer) {
        this.pathName = pathName;
        this.writer = writer;
    }

    private static int h = 0x811c9dc5;

    private static int doHash(final byte[] bytes) {
        for (final byte b : bytes) {
            h = (h * 0x01000193) ^ (b & 0xff);
        }
        return h;
    }

    public void writeResult(int hash, Path path) {
        try {
            String text = String.format("%08x", hash) + " " + path.toString();
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("IOException here: " + e.getMessage());
            System.out.println("Have not been written: " + hash);
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(1024);

        int res = 0x0000000;

        try (ReadableByteChannel in = Files.newByteChannel(file)) {
            h = 0x811c9dc5;
            res = h;
            in.read(buff);
            while (buff.remaining() != buff.capacity()) {
                res = doHash(Arrays.copyOf(buff.array(), buff.position()));
                buff.clear();
                in.read(buff);
            }
        } catch (IOException e) {
            System.out.println("IOException occurred in here: " + e.getMessage());
            System.out.println("While walking a path: " + pathName);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path" + e.getMessage());
            System.out.println("While walking a path: " + pathName);
        } finally {
            writeResult(res, file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        writeResult(0, file);
        System.out.println("you succ");
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc == null) {return FileVisitResult.CONTINUE;}
        else {System.out.println("you succ"); return FileVisitResult.CONTINUE;}
    }
}