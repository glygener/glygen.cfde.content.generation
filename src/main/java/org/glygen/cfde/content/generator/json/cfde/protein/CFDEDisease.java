package org.glygen.cfde.content.generator.json.cfde.protein;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFDEDisease
{
    private String m_id = null;
    private String m_name = null;

    @JsonProperty("DO_ID")
    public String getId()
    {
        return this.m_id;
    }

    public void setId(String a_id)
    {
        this.m_id = a_id;
    }

    @JsonProperty("DISEASE_NAME")
    public String getName()
    {
        return this.m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }
}
