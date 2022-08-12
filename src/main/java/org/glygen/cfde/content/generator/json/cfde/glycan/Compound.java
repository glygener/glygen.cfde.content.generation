package org.glygen.cfde.content.generator.json.cfde.glycan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Compound
{
    private String m_glyTouCan = null;
    private String m_image = null;
    private String m_linkOut = null;
    private String m_pubchem = null;
    private Double m_mass = null;
    private String m_composition = null;
    private List<CFDEMotif> m_motifs = null;
    private List<CFDESpecies> m_species = null;

    @JsonProperty("GLYTOUCAN_ID")
    public String getGlyTouCan()
    {
        return this.m_glyTouCan;
    }

    public void setGlyTouCan(String a_glyTouCan)
    {
        this.m_glyTouCan = a_glyTouCan;
    }

    @JsonProperty("IMAGE_URL")
    public String getImage()
    {
        return this.m_image;
    }

    public void setImage(String a_image)
    {
        this.m_image = a_image;
    }

    @JsonProperty("LINK_OUT_URL")
    public String getLinkOut()
    {
        return this.m_linkOut;
    }

    public void setLinkOut(String a_linkOut)
    {
        this.m_linkOut = a_linkOut;
    }

    @JsonProperty("PUBCHEM_CID")
    public String getPubchem()
    {
        return this.m_pubchem;
    }

    public void setPubchem(String a_pubchem)
    {
        this.m_pubchem = a_pubchem;
    }

    @JsonProperty("MASS")
    public Double getMass()
    {
        return this.m_mass;
    }

    public void setMass(Double a_mass)
    {
        this.m_mass = a_mass;
    }

    @JsonProperty("COMPOSITION")
    public String getComposition()
    {
        return this.m_composition;
    }

    public void setComposition(String a_composition)
    {
        this.m_composition = a_composition;
    }

    @JsonProperty("motifs")
    public List<CFDEMotif> getMotifs()
    {
        return this.m_motifs;
    }

    public void setMotifs(List<CFDEMotif> a_motifs)
    {
        this.m_motifs = a_motifs;
    }

    @JsonProperty("species")
    public List<CFDESpecies> getSpecies()
    {
        return this.m_species;
    }

    public void setSpecies(List<CFDESpecies> a_species)
    {
        this.m_species = a_species;
    }

}
