package org.glygen.cfde.content.generator.json.cfde.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFDESpecies
{
    private String m_name = null;
    private Integer m_taxon = null;

    @JsonProperty("SPECIES_NAME")
    public String getName()
    {
        return this.m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }

    @JsonProperty("ncbi_tax_id")
    public Integer getTaxon()
    {
        return this.m_taxon;
    }

    public void setTaxon(Integer a_taxon)
    {
        this.m_taxon = a_taxon;
    }
}
