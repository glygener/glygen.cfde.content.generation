package org.glygen.cfde.content.generator.json.glygen.protein;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefSeq
{
    private String m_refSeqId = null;
    private String m_reSeqName = null;

    @JsonProperty("ac")
    public String getRefSeqId()
    {
        return this.m_refSeqId;
    }

    public void setRefSeqId(String a_refSeqId)
    {
        this.m_refSeqId = a_refSeqId;
    }

    @JsonProperty("name")
    public String getReSeqName()
    {
        return this.m_reSeqName;
    }

    public void setReSeqName(String a_reSeqName)
    {
        this.m_reSeqName = a_reSeqName;
    }
}
