/*
 * Généré par Hibernate Tools ${version} le ${date}
 * avec FreeMarker ${.version}
 */
package ${daoPackage};

import ${pojo.getQualifiedDeclarationName()};

<#assign classbody>
<#assign pkFqcn = c2j.getJavaTypeName(clazz.identifierProperty, jdk5) />
<#assign shouldImportIdType = pojo.importType(pkFqcn) />
<#assign pkType = pojo.getJavaTypeName(clazz.identifierProperty, true) />
<#assign entityName = pojo.getDeclarationName() />
/**
 * Repository Spring Data JPA pour l'entité {@link ${entityName}}.
 *
 * @see JpaRepository
 */
@${pojo.importType("javax.annotation.Generated")}(value = "Généré par Hibernate Tools ${version}", date = "${.now?iso_local}")
@${pojo.importType("org.springframework.stereotype.Repository")}
@${pojo.importType("org.springframework.transaction.annotation.Transactional")}
@SuppressWarnings("serial")
public interface ${entityName}Repository
    extends ${pojo.importType("org.springframework.data.jpa.repository.JpaRepository")}<${pojo.getDeclarationName()}, ${pkFqcn}>
{
<#-- Générer une méthode pour Java 8 : Stream<T> streamAll(); -->
<#if pojo.hasMetaAttribute("gen-finder-java8-stream")>

    @${pojo.importType("org.springframework.data.jpa.repository.Query")}("select o from ${entityName} o")
    ${pojo.importType("java.util.stream.Stream")}<${entityName}> streamAll();
</#if>
<#-- Générer un finder JPA pour cette property -->
<#-- Doc Spring Data JPA : Defining query methods (https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details) -->
<#foreach property in pojo.getAllPropertiesIterator()>
<#assign propertyJavaType = pojo.getJavaTypeName(property, jdk5) />
<#-- Check incompatible meta attributes -->
<#if c2j.hasMetaAttribute(property, "gen-finder") && c2j.hasMetaAttribute(property, "gen-finder-unique")>
    <#stop "Meta attributes \"gen-finder\" and \"gen-finder-unique\" are mutually exclusives ! - Property is '${property.name}.'">
</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder")>

    /**
     * Finder pour : ${property.name}.
     *
     * @param ${property.name} critère recherche (égalité).
     * @return {@link List} de résultats.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}(${propertyJavaType} ${property.name});

    /**
     * Finder pour : ${property.name}, avec pagination. 
	 * <br/>
	 * Le résultat contient des informations détaillées.
	 * Par conséquent dans ce cas, une requête <code>SQL COUNT</code> sera nécessaire.
     * <p>
	 * Informations complémentaires dans <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.special-parameters">doc Spring Data JPA</a>.
	 * </p>
	 * Extract from online doc :
	 * <pre>
	 * To find out how many pages you get for a query entirely you have to trigger an additional count query. 
	 * By default this query will be derived from the query you actually trigger.
	 * </pre>
     * @param ${property.name} critère recherche (égalité).
     * @param pageable Requête page, optionnellement avec tri ({@link Pageable#getSort()}).
     * @return {@link Page} de résultats, avec informations détaillées (nombre total de pages, nombre total de résultats).
     */
    ${pojo.importType("org.springframework.data.domain.Page")}<${entityName}> findBy${property.name?cap_first}(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Pageable")} pageable);

<#-- *** Conflicts with other method in compiler : both methods signature have same erasure ***
    See https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.4.2
    /**
     * Finder pour : ${property.name}, avec pagination.
	 * <br/>
	 * Le résultat contient seulement les données (aucune métadonnée).
	 * Par conséquent dans ce cas, pas de requête <code>SQL COUNT</code> exécutée.
     *
     * @param ${property.name} critère recherche (égalité).
     * @param pageable Requête page, optionnellement avec tri ({@link Pageable#getSort()}).
     * @return {@link List} de résultats.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Pageable")} pageable);
-->
    /**
     * Finder pour : ${property.name}, avec tri.
     *
     * @param ${property.name} critère recherche (égalité).
     * @param sort Options de tri.
     * @return {@link List} de résultats.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Sort")} sort);

</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder-ignore-case")>
    /**
     * Finder pour : ${property.name}, insensible à la casse.
     *
     * @param ${property.name} critère recherche (égalité).
     * @return {@link List} de résultats.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}IgnoreCase(${propertyJavaType} ${property.name});

</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder-java8-stream")>
    @${pojo.importType("org.springframework.data.jpa.repository.Query")}("select o from ${entityName} o where o.${property.name} = :${property.name}")
    ${pojo.importType("java.util.stream.Stream")}<${entityName}> streamBy${property.name?cap_first}(@${pojo.importType("org.springframework.data.repository.query.Param")}("${property.name}") ${propertyJavaType} ${property.name});

</#if>
<#-- Mutually exclusive with 'gen-finder' -->
<#if c2j.hasMetaAttribute(property, "gen-finder-unique")>
    /**
     * Finder pour : ${property.name}.
     *
     * @param ${property.name} critère recherche (égalité).
     * @return Résultat unique.
     */
     ${entityName} findBy${property.name?cap_first}(${propertyJavaType} ${property.name});

    /**
     * Finder pour : ${property.name}, insensible à la casse.
     *
     * @param ${property.name} critère recherche (égalité + ignore case).
     * @return Résultat unique.
     */
     ${entityName} findBy${property.name?cap_first}IgnoreCase(${propertyJavaType} ${property.name});

</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder-like")>
    /**
     * Finder pour : ${property.name}, avec clause SQL LIKE.
     *
     * @param ${property.name} critère recherche (like) : peut contenir des caractères génériques comme '%' ou '_'.
     * @return Liste de résultats.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}Like(${propertyJavaType} ${property.name});

    /**
     * Finder pour : ${property.name}, avec clause SQL LIKE, avec pagination.
     */
    ${pojo.importType("org.springframework.data.domain.Page")}<${entityName}> findBy${property.name?cap_first}Like(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Pageable")} pageable);

    /**
     * Finder pour : ${property.name}, avec clause SQL LIKE, avec tri.
     */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}Like(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Sort")} sort);

</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder-like-ignore-case")>
    /**
    * Finder pour : ${property.name}, avec clause SQL LIKE, insensible à la casse.
    *
    * @param ${property.name} critère recherche (like) : peut contenir des caractères génériques comme '%' ou '_'.
    * @return Liste de résultats.
    */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}LikeIgnoreCase(${propertyJavaType} ${property.name});

    /**
    * Finder pour : ${property.name}, avec clause SQL LIKE, insensible à la casse, avec pagination.
    */
    ${pojo.importType("org.springframework.data.domain.Page")}<${entityName}> findBy${property.name?cap_first}LikeIgnoreCase(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Pageable")} pageable);

    /**
    * Finder pour : ${property.name}, avec clause SQL LIKE, insensible à la casse, avec tri.
    */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}LikeIgnoreCase(${propertyJavaType} ${property.name}, final ${pojo.importType("org.springframework.data.domain.Sort")} sort);

</#if>
<#if c2j.hasMetaAttribute(property, "gen-finder-between")>
    /**
    * Finder pour : ${property.name}, avec clause SQL BETWEEN.
    *
    * @param startVal critère debut.
    * @param endVal critère fin.
    * @return Liste de résultats.
    */
    ${pojo.importType("java.util.List")}<${entityName}> findBy${property.name?cap_first}Between(${propertyJavaType} startVal, ${propertyJavaType} endVal);

</#if>
</#foreach>
}
</#assign>
${pojo.generateImports()}
${classbody}