package org.glygen.cfde.content.generator.json.glygen.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Glycosylation
{
    private String m_category = null;
    private String m_type = null;
    private Integer m_startPos = null;
    private Integer m_endPos = null;

    @JsonProperty("site_category")
    public String getCategory()
    {
        return this.m_category;
    }

    public void setCategory(String a_category)
    {
        this.m_category = a_category;
    }

    @JsonProperty("type")
    public String getType()
    {
        return this.m_type;
    }

    public void setType(String a_type)
    {
        this.m_type = a_type;
    }

    @JsonProperty("start_pos")
    public Integer getStartPos()
    {
        return this.m_startPos;
    }

    public void setStartPos(Integer a_startPos)
    {
        this.m_startPos = a_startPos;
    }

    @JsonProperty("end_pos")
    public Integer getEndPos()
    {
        return this.m_endPos;
    }

    public void setEndPos(Integer a_endPos)
    {
        this.m_endPos = a_endPos;
    }

}
