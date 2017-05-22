# Hibernate Tools - Reverse Engineering for Pagila DB (PostgreSQL)

## Maven command

`mvn clean generate-sources -Pgen-hibernate-tools`

As a result, code will be generated in _target/generated-sources/hibernate_.

## Running the database server

You can use a docker container, like this one :

`docker run -d -pxxxx:5432 mujz/pagila`

## Templates (FreeMarker)

- pojo : **pojo/Pojo.ftl**
- dao (repository) : **dao/JpaRepository.ftl**

## Tips

### TODO: Force use of object types for @Id and not primitive types  
See _org.hibernate.tool.hbm2x.Cfg2JavaTool.PRIMITIVES_
  
### Defining sequence mapping
Use something like
 
```
    <primary-key>
        <!-- See : org.hibernate.id.enhanced.SequenceStyleGenerator.SEQUENCE_PARAM -->
        <generator class="sequence">
            <param name="sequence_name">category_category_id_seq</param>
        </generator>
        <key-column name="category_id"></key-column>
    </primary-key>
``` 
The name of the param must be one of the constants defined here : [SequenceStyleGenerator](https://docs.jboss.org/hibernate/orm/current/javadocs/org/hibernate/id/enhanced/SequenceStyleGenerator.html).   
The code generation can be explored there : [org.hibernate.tool.hbm2x.pojo.EntityPOJOClass#generateAnnIdGenerator()](https://github.com/hibernate/hibernate-tools/blob/master/main/src/java/org/hibernate/tool/hbm2x/pojo/EntityPOJOClass.java). 

## External references

- [Hibernate Tools](http://hibernate.org/tools/)
- [FreeMarker](http://freemarker.org/)
