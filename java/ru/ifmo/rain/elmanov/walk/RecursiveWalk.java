package ru.ifmo.rain.elmanov.walk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class RecursiveWalk {
    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Invalid input");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF8"))) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF8"))) {
                for (String address = reader.readLine(); address != null; address = reader.readLine()) {
                    try {
                        Files.walkFileTree(Paths.get(address), new MyFileVisitor(address, writer));
                    } catch (SecurityException e) {
                        System.out.println("SecurityException here: " + e.getMessage());

                        String text = String.format("%08x", 0) + " " + address;
                        writer.write(text);
                        writer.newLine();
                    } catch (IOException e) {
                        System.out.println("IOException here: " + e.getMessage());

                        String text = String.format("%08x", 0) + " " + address;
                        writer.write(text);
                        writer.newLine();
                    } catch (InvalidPathException e) {
                        System.out.println("InvalidPathException here: " + e.getMessage());

                        String text = String.format("%08x", 0) + " " + address;
                        writer.write(text);
                        writer.newLine();
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException here: " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                System.out.println("UnsupportedEncodingException here: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException here: " + e.getMessage());
            } catch (SecurityException e) {
                System.out.println("SecurityException here: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException here: " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException here: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException here: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException here: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("SecurityException here: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException here: " + e.getMessage());
        }
    }
}
