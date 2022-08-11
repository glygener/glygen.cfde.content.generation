package org.glygen.cfde.content.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.glygen.api.util.GlyGenDownloader;

public class GlyGenAPIdownload
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Integer t_counter = 1;
        GlyGenDownloader t_downloader = new GlyGenDownloader();
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("./data/glycan.txt"));
        String line = reader.readLine();
        FileWriter t_errorLog = new FileWriter("./data/download.error.log");
        while (line != null)
        {
            line = line.trim();
            if (line.length() > 1)
            {
                String t_fileName = "./data/output/glycan/" + line + ".json";
                File t_file = new File(t_fileName);
                if (!t_file.exists())
                {
                    try
                    {
                        byte[] t_bytes = t_downloader
                                .downloadFile("https://api.glygen.org/glycan/detail/" + line);
                        FileOutputStream t_fileWriter = new FileOutputStream(t_fileName);
                        t_fileWriter.write(t_bytes);
                        t_fileWriter.flush();
                        t_fileWriter.close();
                        System.out.println("Finished glycan: " + t_counter++);
                    }
                    catch (Exception e)
                    {
                        t_errorLog.write("Error for glycan " + line + ": " + e.getMessage() + "\n");
                        t_counter++;
                    }
                    Thread.sleep(400);
                }
            }
            // read next line
            line = reader.readLine();
        }
        reader.close();
        System.out.println("Glycans finished.");
        // protein
        t_counter = 1;
        reader = new BufferedReader(new FileReader("./data/protein.txt"));
        line = reader.readLine();
        while (line != null)
        {
            line = line.trim();
            if (line.length() > 1)
            {
                String t_fileName = "./data/output/protein/" + line + ".json";
                File t_file = new File(t_fileName);
                if (!t_file.exists())
                {
                    try
                    {
                        byte[] t_bytes = t_downloader
                                .downloadFile("https://api.glygen.org/protein/detail/" + line);
                        FileOutputStream t_fileWriter = new FileOutputStream(t_fileName);
                        t_fileWriter.write(t_bytes);
                        t_fileWriter.flush();
                        t_fileWriter.close();
                        System.out.println("Finished protein: " + t_counter++);
                    }
                    catch (Exception e)
                    {
                        t_errorLog
                                .write("Error for protein " + line + ": " + e.getMessage() + "\n");
                        t_counter++;
                    }
                    Thread.sleep(400);
                }
            }
            // read next line
            line = reader.readLine();
        }
        reader.close();
        t_errorLog.flush();
        t_errorLog.close();
    }

}
