package org.glygen.cfde.content.generator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glygen.cfde.content.generator.json.MarkDownEntry;
import org.glygen.cfde.content.generator.json.cfde.glycan.CFDEMotif;
import org.glygen.cfde.content.generator.json.cfde.glycan.CFDESpecies;
import org.glygen.cfde.content.generator.json.cfde.glycan.Compound;
import org.glygen.cfde.content.generator.json.glygen.glycan.Glycan;
import org.glygen.cfde.content.generator.json.glygen.glycan.Motif;
import org.glygen.cfde.content.generator.json.glygen.glycan.Species;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerUtil
{
    private Configuration m_config = null;
    private FileWriter m_errorLog = null;
    private List<MarkDownEntry> m_markdownEntries = new ArrayList<>();
    private List<Compound> m_compounds = new ArrayList<>();

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

    public void buildMarkDownEntry(Glycan a_glycanInfo, String a_glyTouCan, String a_pubChem)
            throws IOException, TemplateException
    {
        GlycanCompositionUtil t_compositionUtil = new GlycanCompositionUtil();
        Compound t_compound = new Compound();
        Map<String, Object> t_mapping = new HashMap<String, Object>();
        t_mapping.put("glyTouCanId", a_glyTouCan);
        t_compound.setGlyTouCan(a_glyTouCan);
        t_compound.setImage("https://api.glygen.org/glycan/image/" + a_glyTouCan);
        t_compound.setLinkOut("https://www.glygen.org/glycan/" + a_glyTouCan);
        if (a_pubChem != null)
        {
            t_mapping.put("pubChemId", a_pubChem);
            t_compound.setPubchem(a_pubChem);
        }
        if (a_glycanInfo.getMass() != null)
        {
            t_mapping.put("mass", a_glycanInfo.getMass());
            t_compound.setMass(a_glycanInfo.getMass());
        }
        if (a_glycanInfo.getComposition() != null && a_glycanInfo.getComposition().size() > 0)
        {
            try
            {
                String t_compostion = t_compositionUtil
                        .buildCompositionString(a_glycanInfo.getComposition());
                t_mapping.put("composition", t_compostion);
                t_compound.setComposition(t_compostion);
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
            List<CFDEMotif> t_motifs = new ArrayList<>();
            for (Motif t_glygenMotif : a_glycanInfo.getMotifs())
            {
                CFDEMotif t_cfdeMotif = new CFDEMotif();
                t_cfdeMotif.setName(t_glygenMotif.getName());
                t_cfdeMotif.setUrl("https://www.glygen.org/motif/" + t_glygenMotif.getId());
                t_motifs.add(t_cfdeMotif);
            }
            t_compound.setMotifs(t_motifs);
        }
        if (a_glycanInfo.getSpecies() != null & a_glycanInfo.getSpecies().size() > 0)
        {
            t_mapping.put("organism", a_glycanInfo.getSpecies());
            List<CFDESpecies> t_species = new ArrayList<>();
            for (Species t_glygenSpecies : a_glycanInfo.getSpecies())
            {
                CFDESpecies t_organism = new CFDESpecies();
                t_organism.setName(t_glygenSpecies.getName());
                t_organism.setTaxon(t_glygenSpecies.getTaxId());
                t_species.add(t_organism);
            }
            t_compound.setSpecies(t_species);
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
        this.m_markdownEntries.add(t_entry);
        this.m_compounds.add(t_compound);
    }

    public List<MarkDownEntry> getMarkdownEntries()
    {
        return this.m_markdownEntries;
    }

    public void setMarkdownEntries(List<MarkDownEntry> a_markdownEntries)
    {
        this.m_markdownEntries = a_markdownEntries;
    }

    public List<Compound> getCompounds()
    {
        return this.m_compounds;
    }

    public void setCompounds(List<Compound> a_compounds)
    {
        this.m_compounds = a_compounds;
    }
}
