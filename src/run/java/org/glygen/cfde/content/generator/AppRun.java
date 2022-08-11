package org.glygen.cfde.content.generator;

import java.io.IOException;

// https://github.com/nih-cfde/knowledge-base-deposit
public class AppRun
{
    public static void main(String[] args) throws IOException
    {
        String t_comandLine = "-g ./data/glycan.txt -p ./data/protein.txt -j ./data/output/ -o ./data/output/";
        String[] t_args = t_comandLine.split(" ");
        App.main(t_args);
    }
}
