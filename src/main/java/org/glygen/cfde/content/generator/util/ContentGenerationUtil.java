package org.glygen.cfde.content.generator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.glygen.cfde.content.generator.json.glygen.glycan.Glycan;
import org.glygen.cfde.content.generator.json.glygen.protein.Protein;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContentGenerationUtil
{
    private FileWriter m_errorLog = null;

    public ContentGenerationUtil(String a_outputFolder) throws IOException
    {
        this.m_errorLog = new FileWriter(a_outputFolder + File.separator + "markdown.error.log");
    }

    public void generateGlycanContent(String a_glycanFile, String a_jsonFolder,
            String a_outputFolder) throws IOException
    {
        FreemarkerUtil t_freemaker = new FreemarkerUtil("./data/templates/", this.m_errorLog);
        BufferedReader t_glycanReader = new BufferedReader(new FileReader(a_glycanFile));
        String t_line = t_glycanReader.readLine();
        while (t_line != null)
        {
            t_line = t_line.trim();
            if (t_line.length() > 0)
            {
                // System.out.println(t_counter++);

                String[] t_ids = t_line.split(" ");
                String t_glyTouCan = t_ids[0];
                String t_pubChem = null;
                if (t_ids.length == 2)
                {
                    t_pubChem = t_ids[1];
                }
                String t_jsonFileName = a_jsonFolder + File.separator + "glycan" + File.separator
                        + t_glyTouCan + ".json";
                File t_jsonFile = new File(t_jsonFileName);
                if (!t_jsonFile.exists())
                {
                    this.m_errorLog.write("Missing JSON file for Glycan: " + t_glyTouCan + "/n");
                }
                else
                {
                    try
                    {
                        Path t_filePath = Path.of(t_jsonFileName);
                        String t_json = Files.readString(t_filePath);
                        ObjectMapper t_mapper = new ObjectMapper();
                        Glycan t_glycanInfo = t_mapper.readValue(t_json, Glycan.class);
                        t_freemaker.buildMarkDownEntry(t_glycanInfo, t_glyTouCan, t_pubChem);
                    }
                    catch (Exception e)
                    {
                        this.m_errorLog.write("Unable generate markdown for " + t_glyTouCan + ": "
                                + e.getMessage() + "/n");
                    }
                }
            }
            t_line = t_glycanReader.readLine();
        }
        t_glycanReader.close();
        // create the JSON string
        ObjectMapper t_mapper = new ObjectMapper();
        t_mapper.setSerializationInclusion(Include.NON_NULL);
        t_mapper.writeValue(new File(a_outputFolder + File.separator + "compound.md.json"),
                t_freemaker.getMarkdownEntries());
        t_mapper = new ObjectMapper();
        t_mapper.setSerializationInclusion(Include.NON_NULL);
        String t_json = t_mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(t_freemaker.getCompounds());
        FileWriter t_writer = new FileWriter(
                new File(a_outputFolder + File.separator + "compound.data.json"));
        t_writer.write(t_json);
        t_writer.flush();
        t_writer.close();
    }

    public void writeErrorLog() throws IOException
    {
        this.m_errorLog.flush();
        this.m_errorLog.close();
    }

    public void generateProteinContent(String a_proteinFile, String a_jsonFolder,
            String a_outputFolder) throws IOException
    {
        FreemarkerUtil t_freemaker = new FreemarkerUtil("./data/templates/", this.m_errorLog);
        BufferedReader t_proteinReader = new BufferedReader(new FileReader(a_proteinFile));
        String t_proteinId = t_proteinReader.readLine();
        while (t_proteinId != null)
        {
            t_proteinId = t_proteinId.trim();
            if (t_proteinId.length() > 0)
            {
                String t_jsonFileName = a_jsonFolder + File.separator + "protein" + File.separator
                        + t_proteinId + ".json";
                File t_jsonFile = new File(t_jsonFileName);
                if (!t_jsonFile.exists())
                {
                    this.m_errorLog.write("Missing JSON file for Protein: " + t_proteinId + "/n");
                }
                else
                {
                    try
                    {
                        Path t_filePath = Path.of(t_jsonFileName);
                        String t_json = Files.readString(t_filePath);
                        ObjectMapper t_mapper = new ObjectMapper();
                        Protein t_proteinInfo = t_mapper.readValue(t_json, Protein.class);
                        t_freemaker.buildMarkDownEntry(t_proteinInfo, t_proteinId);
                    }
                    catch (Exception e)
                    {
                        this.m_errorLog.write("Unable generate markdown for " + t_proteinId + ": "
                                + e.getMessage() + "/n");
                    }
                }
            }
            t_proteinId = t_proteinReader.readLine();
        }
        t_proteinReader.close();
        // create the JSON string
        ObjectMapper t_mapper = new ObjectMapper();
        t_mapper.setSerializationInclusion(Include.NON_NULL);
        t_mapper.writeValue(new File(a_outputFolder + File.separator + "protein.md.json"),
                t_freemaker.getMarkdownEntries());
        t_mapper = new ObjectMapper();
        t_mapper.setSerializationInclusion(Include.NON_NULL);
        String t_json = t_mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(t_freemaker.getProteins());
        FileWriter t_writer = new FileWriter(
                new File(a_outputFolder + File.separator + "protein.data.json"));
        t_writer.write(t_json);
        t_writer.flush();
        t_writer.close();
    }
}
