/**
 * Contains all the DB migrations needed to fill database for current version of the "application". All those files are
 * <a href="http://www.liquibase.org/">Liquibase</a> XML description of what SQL statements should be issued. <p/>
 * Note, that there is a single file that imports all the migrations: {@code IncludeAllDbMigrations.xml} which is
 * imported by Spring bean during the app startup. This ensures that the whole DB structure is initiated before we're
 * starting our app or tests.
 */
package org.javatalks.training.hibernate.dbschema;