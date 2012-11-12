/**
 * This should demonstrate how to work with pure JDBC in order to compare it with Hibernate and other approaches. But we
 * don't actually want to create connections on our own and can use Spring Transaction support to help us, because this
 * is actually what doesn't change regardless whether we're using ORM or JDBC.
 *
 * @author stanislav bashkirtsev
 */
package org.javatalks.training.hibernate.jdbcdao;