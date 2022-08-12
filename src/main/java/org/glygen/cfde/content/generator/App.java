package org.glygen.cfde.content.generator;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glygen.cfde.content.generator.util.ContentGenerationUtil;

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
        ContentGenerationUtil t_util = new ContentGenerationUtil(t_arguments.getOutputFolder());

        t_util.generateGlycanContent(t_arguments.getGlycanFile(), t_arguments.getJsonFolder(),
                t_arguments.getOutputFolder());

        t_util.generateProteinContent(t_arguments.getProteinFile(), t_arguments.getJsonFolder(),
                t_arguments.getOutputFolder());

        t_util.writeErrorLog();
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
