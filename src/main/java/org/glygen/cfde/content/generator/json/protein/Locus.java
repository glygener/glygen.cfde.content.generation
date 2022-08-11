package org.glygen.cfde.content.generator.json.protein;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Locus
{
    private Long m_startPos = null;
    private Long m_endPos = null;
    private String m_chromosome = null;
    private List<Evidence> m_evidence = new ArrayList<>();

    @JsonProperty("start_pos")
    public Long getStartPos()
    {
        return this.m_startPos;
    }

    public void setStartPos(Long a_startPos)
    {
        this.m_startPos = a_startPos;
    }

    @JsonProperty("end_pos")
    public Long getEndPos()
    {
        return this.m_endPos;
    }

    public void setEndPos(Long a_endPos)
    {
        this.m_endPos = a_endPos;
    }

    @JsonProperty("chromosome")
    public String getChromosome()
    {
        return this.m_chromosome;
    }

    public void setChromosome(String a_chromosome)
    {
        this.m_chromosome = a_chromosome;
    }

    @JsonProperty("evidence")
    public List<Evidence> getEvidence()
    {
        return m_evidence;
    }

    public void setEvidence(List<Evidence> a_evidence)
    {
        this.m_evidence = a_evidence;
    }
}
