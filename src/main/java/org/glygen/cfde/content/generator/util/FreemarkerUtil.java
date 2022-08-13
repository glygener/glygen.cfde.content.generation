package org.glygen.cfde.content.generator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.glygen.cfde.content.generator.json.MarkDownEntry;
import org.glygen.cfde.content.generator.json.cfde.glycan.CFDEMotif;
import org.glygen.cfde.content.generator.json.cfde.glycan.CFDESpecies;
import org.glygen.cfde.content.generator.json.cfde.glycan.Compound;
import org.glygen.cfde.content.generator.json.cfde.protein.CFDEDisease;
import org.glygen.cfde.content.generator.json.cfde.protein.CFDEProtein;
import org.glygen.cfde.content.generator.json.glygen.glycan.Glycan;
import org.glygen.cfde.content.generator.json.glygen.glycan.Motif;
import org.glygen.cfde.content.generator.json.glygen.glycan.Species;
import org.glygen.cfde.content.generator.json.glygen.protein.Disease;
import org.glygen.cfde.content.generator.json.glygen.protein.Evidence;
import org.glygen.cfde.content.generator.json.glygen.protein.Gene;
import org.glygen.cfde.content.generator.json.glygen.protein.Glycosylation;
import org.glygen.cfde.content.generator.json.glygen.protein.Locus;
import org.glygen.cfde.content.generator.json.glygen.protein.Protein;

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
    private List<CFDEProtein> m_proteins = new ArrayList<>();

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
        if (a_glycanInfo.getMotifs() != null && a_glycanInfo.getMotifs().size() > 0)
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
        if (a_glycanInfo.getSpecies() != null && a_glycanInfo.getSpecies().size() > 0)
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

    public List<CFDEProtein> getProteins()
    {
        return this.m_proteins;
    }

    public void setProteins(List<CFDEProtein> a_proteins)
    {
        this.m_proteins = a_proteins;
    }

    public void buildMarkDownEntry(Protein a_proteinInfo, String a_proteinId)
            throws IOException, TemplateException
    {
        CFDEProtein t_cfdeProtein = new CFDEProtein();
        Map<String, Object> t_mapping = new HashMap<String, Object>();
        t_mapping.put("uniprotAc", a_proteinId);
        t_cfdeProtein.setUniprotAc(a_proteinId);
        t_cfdeProtein.setLinkout("https://www.glygen.org/protein/" + a_proteinId);
        if (a_proteinInfo.getRefSeq() != null)
        {
            String t_value = a_proteinInfo.getRefSeq().getRefSeqId();
            if (t_value != null)
            {
                t_mapping.put("refseqId", t_value);
                t_cfdeProtein.setRefseqId(t_value);
            }
            t_value = a_proteinInfo.getRefSeq().getReSeqName();
            if (t_value != null)
            {
                t_mapping.put("refseqName", t_value);
                t_cfdeProtein.setRefseqName(t_value);
            }
        }
        if (a_proteinInfo.getGene() != null)
        {
            if (a_proteinInfo.getGene().size() > 1)
            {
                this.m_errorLog.write("Too many gene objects for " + a_proteinId + ": "
                        + Integer.toString(a_proteinInfo.getGene().size()) + "\n");
            }
            for (Gene t_gene : a_proteinInfo.getGene())
            {
                if (t_gene.getName() != null)
                {
                    t_mapping.put("geneName", t_gene.getName());
                    t_cfdeProtein.setGeneName(t_gene.getName());
                }
                if (t_gene.getLocus() != null)
                {
                    try
                    {
                        String t_result = "Chromosome: ";
                        Locus t_locus = t_gene.getLocus();
                        DecimalFormat t_decimalFormat = new DecimalFormat("#.##");
                        t_decimalFormat.setGroupingUsed(true);
                        t_decimalFormat.setGroupingSize(3);
                        t_result = t_result + t_locus.getChromosome() + "("
                                + t_decimalFormat.format(t_locus.getStartPos()) + " - "
                                + t_decimalFormat.format(t_locus.getEndPos()) + ")";
                        t_mapping.put("location", t_result);
                        t_cfdeProtein.setGeneLocation(t_result);
                    }
                    catch (Exception e)
                    {
                        this.m_errorLog.write("Error creating location for " + a_proteinId + ": "
                                + e.getMessage() + "\n");
                    }
                    if (t_gene.getLocus().getEvidence() != null)
                    {
                        try
                        {
                            for (Evidence t_evidence : t_gene.getLocus().getEvidence())
                            {
                                if (t_evidence.getDatabase().equals("Ensembl Gene"))
                                {
                                    t_mapping.put("geneId", t_evidence.getId());
                                    t_cfdeProtein.setEnsemblId(t_evidence.getId());
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            this.m_errorLog.write("Error finding ensembl gene ID for " + a_proteinId
                                    + ": " + e.getMessage() + "\n");
                        }
                    }
                }
            }
        }
        if (a_proteinInfo.getDiseases() != null && a_proteinInfo.getDiseases().size() > 0)
        {
            List<CFDEDisease> t_cfdeDiseases = new ArrayList<>();
            for (Disease t_disease : a_proteinInfo.getDiseases())
            {
                CFDEDisease t_cfdeDisease = new CFDEDisease();
                t_cfdeDisease.setId(t_disease.getId());
                t_cfdeDisease.setName(t_disease.getName().getName());
                t_cfdeDiseases.add(t_cfdeDisease);
            }
            t_mapping.put("disease", t_cfdeDiseases);
            t_cfdeProtein.setDiseases(t_cfdeDiseases);
        }
        if (a_proteinInfo.getGlycosylation() != null && a_proteinInfo.getGlycosylation().size() > 0)
        {
            HashSet<String> t_sitesN = new HashSet<>();
            HashSet<String> t_sitesO = new HashSet<>();
            Integer t_annotationN = 0;
            Integer t_annotationO = 0;
            for (Glycosylation t_annotation : a_proteinInfo.getGlycosylation())
            {
                if (t_annotation.getType().equals("O-linked"))
                {
                    t_annotationO++;
                    String t_position = "";
                    if (t_annotation.getStartPos() != null)
                    {
                        t_position = t_position + t_annotation.getStartPos();
                    }
                    t_position = t_position + "-";
                    if (t_annotation.getEndPos() != null)
                    {
                        t_position = t_position + t_annotation.getEndPos();
                    }
                    if (!t_sitesO.contains(t_position))
                    {
                        t_sitesO.add(t_position);
                    }
                }
                else if (t_annotation.getType().equals("N-linked"))
                {
                    t_annotationN++;
                    String t_position = "";
                    if (t_annotation.getStartPos() != null)
                    {
                        t_position = t_position + t_annotation.getStartPos();
                    }
                    t_position = t_position + "-";
                    if (t_annotation.getEndPos() != null)
                    {
                        t_position = t_position + t_annotation.getEndPos();
                    }
                    if (!t_sitesN.contains(t_position))
                    {
                        t_sitesN.add(t_position);
                    }
                }
                else
                {
                    this.m_errorLog.write("Unknwon glycosylation type for " + a_proteinId + ": "
                            + t_annotation.getType() + "\n");
                }
            }
            Integer t_totalSite = t_sitesN.size() + t_sitesO.size();
            String t_glycosylation = t_totalSite.toString() + " site(s) total, "
                    + t_annotationN.toString() + " N-linked annotation(s) at "
                    + Integer.toString(t_sitesN.size()) + " site(s), " + t_annotationO.toString()
                    + " O-linked annotation(s) at " + Integer.toString(t_sitesO.size())
                    + " site(s)";
            t_mapping.put("glycoSummary", t_glycosylation);
            t_cfdeProtein.setGlycosylation(t_glycosylation);
        }
        else
        {
            t_mapping.put("glycoSummary", "No glycosylation reported");
            t_cfdeProtein.setGlycosylation("No glycosylation reported");
        }
        String t_result = this.render(t_mapping, "protein.template.ftlh");
        t_result = t_result.replaceAll("\\r", "");
        MarkDownEntry t_entry = new MarkDownEntry();
        t_entry.setMarkdown(t_result);
        t_entry.setId(a_proteinId);
        this.m_markdownEntries.add(t_entry);
        this.m_proteins.add(t_cfdeProtein);
    }

}
