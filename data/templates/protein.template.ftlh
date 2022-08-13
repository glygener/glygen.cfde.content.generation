## Protein Details

**UniProtKB Accession**: [${uniprotAc}](https://www.glygen.org/protein/${uniprotAc})  
**RefSeq ID**: <#if refseqId??>${refseqId}  
</#if><#if refseqName??>**RefSeq Name**: ${refseqName}  
</#if><#if geneName??>**Gene Name**: ${geneName}  
</#if><#if location??>**Gene Location**: ${location}  
</#if><#if geneId??>**Ensembl Gene ID**: ${geneId}  
</#if><#if disease??>**Disease**: <#list disease as disobj>${disobj.name}(${disobj.id})<#sep>, </#list>  
</#if><#if glycoSummary??>**Glycosylation Summary**: ${glycoSummary}  
</#if>

More information for protein [${uniprotAc}](https://www.glygen.org/protein/${uniprotAc}) is available on GlyGen