package org.glygen.cfde.content.generator.json.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gene
{
    private String m_name = null;
    private Locus m_locus = null;

    @JsonProperty("name")
    public String getName()
    {
        return this.m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }

    @JsonProperty("locus")
    public Locus getLocus()
    {
        return this.m_locus;
    }

    public void setLocus(Locus a_locus)
    {
        this.m_locus = a_locus;
    }

}
