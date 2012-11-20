/**
 * Contains all the DB migrations needed to fill database for current version of the "application". All those files are
 * <a href="http://www.liquibase.org/">Liquibase</a> XML description of what SQL statements should be issued. <p/>
 * Each DB since it's created in different ways (PostgreSQL needs sequence to be created while MySQL uses generators),
 * has its own package with migrations. Note, that there is a single file that imports all the migrations in each
 * package: {@code IncludeAll.xml} which is imported by Spring bean during the app startup. This ensures that the whole
 * DB structure is initiated before we're starting our app or tests.
 */
package org.javatalks.training.hibernate.dbschema;