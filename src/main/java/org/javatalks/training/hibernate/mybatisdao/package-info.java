/**
 * Even though we have a mappers level that's done mostly by MyBatis, we still need from time to time something more
 * elaborate that MyBatis can provide. That's why we have a DAO layer that delegates the most basic stuff to MyBatis'
 * Mappers. Note, that DAO classes are implementing Mappers because they need to have same functions plus their own
 * ones.
 */
package org.javatalks.training.hibernate.mybatisdao;