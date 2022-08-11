package org.glygen.cfde.content.generator.json.protein;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Protein
{
    private RefSeq m_refSeq = null;
    private List<Gene> m_gene = new ArrayList<>();
    private List<Disease> m_diseases = new ArrayList<>();
    private List<Glycosylation> m_glycosylation = new ArrayList<>();

    @JsonProperty("refseq")
    public RefSeq getRefSeq()
    {
        return this.m_refSeq;
    }

    public void setRefSeq(RefSeq a_refSeq)
    {
        this.m_refSeq = a_refSeq;
    }

    @JsonProperty("gene")
    public List<Gene> getGene()
    {
        return this.m_gene;
    }

    public void setGene(List<Gene> a_gene)
    {
        this.m_gene = a_gene;
    }

    @JsonProperty("disease")
    public List<Disease> getDiseases()
    {
        return this.m_diseases;
    }

    public void setDiseases(List<Disease> a_diseases)
    {
        this.m_diseases = a_diseases;
    }

    @JsonProperty("glycosylation")
    public List<Glycosylation> getGlycosylation()
    {
        return this.m_glycosylation;
    }

    public void setGlycosylation(List<Glycosylation> a_glycosylation)
    {
        this.m_glycosylation = a_glycosylation;
    }
}
