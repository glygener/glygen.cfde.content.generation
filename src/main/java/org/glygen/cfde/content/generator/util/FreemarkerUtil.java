package org.glygen.cfde.content.generator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.glygen.cfde.content.generator.json.MarkDownEntry;
import org.glygen.cfde.content.generator.json.glycan.Glycan;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerUtil
{
    private Configuration m_config = null;
    private FileWriter m_errorLog = null;

    public FreemarkerUtil(String a_templateFolder, FileWriter a_errorLog) throws IOException
    {
        this.m_config = new Configuration(Configuration.VERSION_2_3_29);
        this.m_config.setDirectoryForTemplateLoading(new File(a_templateFolder));
        this.m_config.setDefaultEncoding("UTF-8");
        this.m_config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.m_config.setLogTemplateExceptions(false);
        this.m_config.setWrapUncheckedExceptions(true);
        this.m_config.setFallbackOnNullLoopVariable(false);

        this.m_errorLog = a_errorLog;
    }

    public String render(Map<String, Object> a_inputObject, String a_templateFile)
            throws IOException, TemplateException
    {
        Template t_template = this.m_config.getTemplate(a_templateFile);
        StringWriter t_writer = new StringWriter();
        t_template.process(a_inputObject, t_writer);
        t_writer.close();
        return t_writer.toString();
    }

    public MarkDownEntry buildMarkDownEntry(Glycan a_glycanInfo, String a_glyTouCan,
            String a_pubChem) throws IOException, TemplateException
    {
        GlycanCompositionUtil t_compositionUtil = new GlycanCompositionUtil();
        Map<String, Object> t_mapping = new HashMap<String, Object>();
        t_mapping.put("glyTouCanId", a_glyTouCan);
        if (a_pubChem != null)
        {
            t_mapping.put("pubChemId", a_pubChem);
        }
        if (a_glycanInfo.getMass() != null)
        {
            t_mapping.put("mass", a_glycanInfo.getMass());
        }
        if (a_glycanInfo.getComposition() != null && a_glycanInfo.getComposition().size() > 0)
        {
            try
            {
                t_mapping.put("composition",
                        t_compositionUtil.buildCompositionString(a_glycanInfo.getComposition()));
            }
            catch (Exception e)
            {
                this.m_errorLog.write("Error generating composition for " + a_glyTouCan + ": "
                        + e.getMessage() + "\n");
            }
        }
        if (a_glycanInfo.getMotifs() != null & a_glycanInfo.getMotifs().size() > 0)
        {
            t_mapping.put("motifs", a_glycanInfo.getMotifs());
        }
        if (a_glycanInfo.getSpecies() != null & a_glycanInfo.getSpecies().size() > 0)
        {
            t_mapping.put("organism", a_glycanInfo.getSpecies());
        }
        String t_result = this.render(t_mapping, "glycan.template.ftlh");
        t_result = t_result.replaceAll("\\r", "");
        MarkDownEntry t_entry = new MarkDownEntry();
        t_entry.setMarkdown(t_result);
        if (a_pubChem == null)
        {
            t_entry.setId(a_glyTouCan);
        }
        else
        {
            t_entry.setId(a_pubChem);
        }
        return t_entry;
    }
}
