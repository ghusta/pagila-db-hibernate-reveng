<#if jdk5?if_exists>
<#if c2j.hasMetaAttribute(property, "hs-field")>
    @${pojo.importType("org.hibernate.search.annotations.Field")}
</#if>
</#if>