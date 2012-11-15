/**
 * Mappers is the layer implemented by MyBatis - we just need to describe interfaces, then describe mapping and SQL
 * statements either by annotations or XML following the convention and MyBatis will implement those interfaces itself,
 * we just need to declare {@link org.mybatis.spring.mapper.MapperFactoryBean} for them in our Spring Context. <p/>
 * Note though that this layer is pretty limited and primitive, sometimes we need something more comprehensive that
 * MyBatis can give us, that's why there is a DAO layer also present, that actually provides API to the rest of project.
 */
package org.javatalks.training.hibernate.mybatisdao.mapper;