package org.glygen.cfde.content.generator.json.cfde.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFDEMotif
{
    private String m_name = null;
    private String m_url = null;

    @JsonProperty("MOTIF_NAME")
    public String getName()
    {
        return this.m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }

    @JsonProperty("MOTIF_URL")
    public String getUrl()
    {
        return this.m_url;
    }

    public void setUrl(String a_url)
    {
        this.m_url = a_url;
    }
}
