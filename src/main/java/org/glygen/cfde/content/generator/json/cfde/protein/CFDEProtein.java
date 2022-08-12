package org.glygen.cfde.content.generator.json.cfde.protein;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFDEProtein
{
    private String m_uniprotAc = null;
    private String m_linkout = null;
    private String m_refseqId = null;
    private String m_refseqName = null;
    private String m_geneName = null;
    private String m_geneLocation = null;
    private String m_ensemblId = null;
    private String m_glycosylation = null;
    private List<CFDEDisease> m_diseases = null;

    @JsonProperty("UNIPROT_AC")
    public String getUniprotAc()
    {
        return this.m_uniprotAc;
    }

    public void setUniprotAc(String a_uniprotAc)
    {
        this.m_uniprotAc = a_uniprotAc;
    }

    @JsonProperty("LINK_OUT_URL")
    public String getLinkout()
    {
        return this.m_linkout;
    }

    public void setLinkout(String a_linkout)
    {
        this.m_linkout = a_linkout;
    }

    @JsonProperty("REFSEQ_ID")
    public String getRefseqId()
    {
        return this.m_refseqId;
    }

    public void setRefseqId(String a_refseqId)
    {
        this.m_refseqId = a_refseqId;
    }

    @JsonProperty("REFSEQ_NAME")
    public String getRefseqName()
    {
        return this.m_refseqName;
    }

    public void setRefseqName(String a_refseqName)
    {
        this.m_refseqName = a_refseqName;
    }

    @JsonProperty("GENE_NAME")
    public String getGeneName()
    {
        return this.m_geneName;
    }

    public void setGeneName(String a_geneName)
    {
        this.m_geneName = a_geneName;
    }

    @JsonProperty("ENSEMBL_ID")
    public String getEnsemblId()
    {
        return this.m_ensemblId;
    }

    public void setEnsemblId(String a_ensemblId)
    {
        this.m_ensemblId = a_ensemblId;
    }

    @JsonProperty("GLYCOSYLATION")
    public String getGlycosylation()
    {
        return this.m_glycosylation;
    }

    public void setGlycosylation(String a_glycosylation)
    {
        this.m_glycosylation = a_glycosylation;
    }

    @JsonProperty("disease")
    public List<CFDEDisease> getDiseases()
    {
        return this.m_diseases;
    }

    public void setDiseases(List<CFDEDisease> a_diseases)
    {
        this.m_diseases = a_diseases;
    }

    @JsonProperty("GENE_LOCATION")
    public String getGeneLocation()
    {
        return m_geneLocation;
    }

    public void setGeneLocation(String a_geneLocation)
    {
        this.m_geneLocation = a_geneLocation;
    }
}
