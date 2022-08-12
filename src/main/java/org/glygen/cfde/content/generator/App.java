package org.glygen.cfde.content.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glygen.cfde.content.generator.json.glygen.glycan.Glycan;
import org.glygen.cfde.content.generator.util.FreemarkerUtil;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App
{

    public static void main(String[] a_args) throws IOException
    {
        // parse the command line arguments and store them
        Options t_options = App.buildComandLineOptions();
        AppArguments t_arguments = App.processCommandlineArguments(a_args, t_options);
        if (t_arguments == null)
        {
            // error messages and command line options have been printed already
            return;
        }
        // glycans
        Integer t_counter = 1;
        FileWriter t_errorLog = new FileWriter(
                t_arguments.getOutputFolder() + File.separator + "markdown.error.log");
        FreemarkerUtil t_freemaker = new FreemarkerUtil("./data/templates/", t_errorLog);
        BufferedReader t_glycanReader = new BufferedReader(
                new FileReader(t_arguments.getGlycanFile()));
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
                String t_jsonFileName = t_arguments.getJsonFolder() + File.separator + "glycan"
                        + File.separator + t_glyTouCan + ".json";
                File t_jsonFile = new File(t_jsonFileName);
                if (!t_jsonFile.exists())
                {
                    t_errorLog.write("Missing JSON file for Glycan: " + t_glyTouCan + "/n");
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
                        t_errorLog.write("Unable generate markdown for " + t_glyTouCan + ": "
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
        t_mapper.writeValue(
                new File(t_arguments.getOutputFolder() + File.separator + "compound.md.json"),
                t_freemaker.getMarkdownEntries());
        t_mapper = new ObjectMapper();
        t_mapper.setSerializationInclusion(Include.NON_NULL);
        String t_json = t_mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(t_freemaker.getCompounds());
        FileWriter t_writer = new FileWriter(
                new File(t_arguments.getOutputFolder() + File.separator + "compound.data.json"));
        t_writer.write(t_json);
        t_writer.flush();
        t_writer.close();
        // t_mapper.writeValue(
        // new File(t_arguments.getOutputFolder() + File.separator +
        // "compound.data.json"),
        // t_freemaker.getCompounds());

        t_errorLog.flush();
        t_errorLog.close();
    }

    /**
     * Process the command line options and create the AppArgument object.
     *
     * If the processing failed the error messages and command line options have
     * been printed.
     *
     * @param a_args
     *            Command line arguments given by the user
     * @param a_options
     *            Configuration object for the command line parameters
     * @return AppArguments object with the extracted command line options or
     *         NULL if parsing/validation failed. In this case error messages
     *         and valid command line options have been printed to System.out.
     */
    private static AppArguments processCommandlineArguments(String[] a_args, Options a_options)
    {
        // initialize the arguments from command line
        AppArguments t_arguments = null;
        try
        {
            t_arguments = App.parseArguments(a_args, a_options);
            if (t_arguments == null)
            {
                // failed, message was printed, time to go
                App.printComandParameter(a_options);
                return null;
            }
        }
        catch (ParseException e)
        {
            System.out.println("Invalid commandline arguments: " + e.getMessage());
            App.printComandParameter(a_options);
            return null;
        }
        catch (Exception e)
        {
            System.out.println(
                    "There was an error processing the command line arguments: " + e.getMessage());
            App.printComandParameter(a_options);
            return null;
        }
        return t_arguments;
    }

    /**
     * Parse the command line parameters or load the values from a properties
     * file. Values are validated as well.
     *
     * @param a_args
     *            Command line parameters handed down to the application.
     * @return Validated parameter object or null if loading/validation fails.
     *         In that case corresponding error message are printed to console.
     * @throws ParseException
     *             Thrown if the command line parsing fails
     */
    private static AppArguments parseArguments(String[] a_args, Options a_options)
            throws ParseException
    {
        // create the command line parser
        CommandLineParser t_parser = new DefaultParser();
        // parse the command line arguments
        CommandLine t_commandLine = t_parser.parse(a_options, a_args);
        AppArguments t_arguments = new AppArguments();
        // overwrite from arguments
        t_arguments.setGlycanFile(t_commandLine.getOptionValue("g"));
        t_arguments.setOutputFolder(t_commandLine.getOptionValue("o"));
        t_arguments.setProteinFile(t_commandLine.getOptionValue("p"));
        t_arguments.setJsonFolder(t_commandLine.getOptionValue("j"));
        // check settings
        if (!App.checkArguments(t_arguments))
        {
            return null;
        }
        return t_arguments;
    }

    /**
     * Check the command line arguments.
     *
     * @param a_arguments
     *            Argument object filled with the parsed command line parameters
     * @return TRUE if the parameters are valid. FALSE if at least one parameter
     *         is incorrect. In that case a message is printed to System.out
     */
    private static boolean checkArguments(AppArguments a_arguments)
    {
        boolean t_valid = true;
        return t_valid;
    }

    /**
     * Print out the command line parameter.
     */
    private static void printComandParameter(Options a_options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "<command> -g <glycanFile> -p <proteinFile> -j <jsonBaseFolder> -o <OutputFolder> ",
                a_options);
    }

    /**
     * Build the command line argument object that contains all options
     *
     * @return Object with the command line options
     */
    private static Options buildComandLineOptions()
    {
        // create the Options
        Options t_options = new Options();
        // configuration file
        Option t_option = new Option("g", "glycan", true,
                "Location of a text file with GlyTouCan IDs.");
        t_option.setArgs(1);
        t_option.setRequired(true);
        t_options.addOption(t_option);
        // output folder
        t_option = new Option("o", "output", true,
                "Output folder that will contain the TSV files.");
        t_option.setArgs(1);
        t_option.setRequired(true);
        t_options.addOption(t_option);
        // properties file
        t_option = new Option("p", "protein", true,
                "Location of a text file with UniProt Accessions.");
        t_option.setArgs(1);
        t_option.setRequired(true);
        t_options.addOption(t_option);
        // json file
        t_option = new Option("j", "json", true,
                "Base folder with the json files from GlyGen. This folder contains a 'glycan' and 'protein' folder.");
        t_option.setArgs(1);
        t_option.setRequired(true);
        t_options.addOption(t_option);

        return t_options;
    }
}
