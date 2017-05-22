<#if jdk5?if_exists>
<#if pojo.hasMetaAttribute("hs-indexed")>
@${pojo.importType("org.hibernate.search.annotations.Indexed")}
</#if>
</#if>