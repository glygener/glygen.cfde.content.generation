package org.glygen.cfde.content.generator;

public class AppArguments
{
    private String m_glycanFile = null;
    private String m_outputFolder = null;
    private String m_proteinFile = null;
    private String m_jsonFolder = null;

    public String getOutputFolder()
    {
        return this.m_outputFolder;
    }

    public void setOutputFolder(String a_outputFolder)
    {
        this.m_outputFolder = a_outputFolder;
    }

    public String getGlycanFile()
    {
        return this.m_glycanFile;
    }

    public void setGlycanFile(String a_glycanFile)
    {
        this.m_glycanFile = a_glycanFile;
    }

    public String getProteinFile()
    {
        return this.m_proteinFile;
    }

    public void setProteinFile(String a_proteinFile)
    {
        this.m_proteinFile = a_proteinFile;
    }

    public String getJsonFolder()
    {
        return this.m_jsonFolder;
    }

    public void setJsonFolder(String a_jsonFolder)
    {
        this.m_jsonFolder = a_jsonFolder;
    }

}
