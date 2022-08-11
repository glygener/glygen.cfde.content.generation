package org.glygen.cfde.content.generator.json.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Disease
{
    private String m_id = null;
    private RecommendedName m_name = null;

    @JsonProperty("disease_id")
    public String getId()
    {
        return this.m_id;
    }

    public void setId(String a_id)
    {
        this.m_id = a_id;
    }

    @JsonProperty("recommended_name")
    public RecommendedName getName()
    {
        return this.m_name;
    }

    public void setName(RecommendedName a_name)
    {
        this.m_name = a_name;
    }
}
