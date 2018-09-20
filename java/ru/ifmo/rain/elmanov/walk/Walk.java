package ru.ifmo.rain.elmanov.walk;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;

public class Walk {
    private static int h = 0x811c9dc5;

    private static int doHash(final byte[] bytes) {
        for (final byte b : bytes) {
            h = (h * 0x01000193) ^ (b & 0xff);
        }
        return h;
    }

    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Invalid input");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF8"))) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF8"))) {
                for (String adress = reader.readLine(); adress != null; adress = reader.readLine()) {
                    ByteBuffer buff = ByteBuffer.allocate(1024);

                    int res = 0x0000000;

                    try (ReadableByteChannel in = Files.newByteChannel(Paths.get(adress))) {
                        h = 0x811c9dc5;
                        res = h;
                        in.read(buff);
                        while (buff.remaining() != buff.capacity()) {
                            res = doHash(Arrays.copyOf(buff.array(), buff.position()));
                            buff.clear();
                            in.read(buff);
                        }
                    } catch (IOException e) {
                        System.out.println("IOException occurred: " + e.getMessage());
                    } catch (InvalidPathException e) {
                        System.out.println("Invalid path" + e.getMessage());
                    } finally {
                        String text = String.format("%08x", res) + " " + adress;
                        writer.write(text);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
            }
        } catch (IOException e) {
        }
    }
}