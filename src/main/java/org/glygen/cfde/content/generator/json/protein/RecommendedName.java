package org.glygen.cfde.content.generator.json.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedName
{
    private String m_name = null;

    @JsonProperty("name")
    public String getName()
    {
        return m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }
}
