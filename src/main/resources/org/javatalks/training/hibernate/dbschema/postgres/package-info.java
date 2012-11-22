/**
 * Note, that there are less files than in MySQL package, that's because we moved here only those files that really
 * need to produce different SQL statements, tables without PK for instance don't need to be duplicated, the generated
 * DDL will be the same as for MySQL because Liquibase won't create SEQ tables for them.
 */
package org.javatalks.training.hibernate.dbschema.postgres;