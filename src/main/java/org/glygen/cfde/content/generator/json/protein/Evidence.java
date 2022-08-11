package org.glygen.cfde.content.generator.json.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Evidence
{
    private String m_id = null;
    private String m_database = null;

    @JsonProperty("id")
    public String getId()
    {
        return this.m_id;
    }

    public void setId(String a_id)
    {
        this.m_id = a_id;
    }

    @JsonProperty("database")
    public String getDatabase()
    {
        return this.m_database;
    }

    public void setDatabase(String a_database)
    {
        this.m_database = a_database;
    }
}
