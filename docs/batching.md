Batch insert looks like:
`insert into TABLENAME(colum1, column2) values (1, 2), (1, 4), (100, 500);`

* hibernate.jdbc.batch_size
* session.setFlushMode(FlushMode.MANUAL)
* session.flush() + session.clear()
* Logging
** Hibernate logging won't show batch effect:
*** hibernate.show_comments=true
*** logger("org.hibernate.type.descriptor.sql.BasicBinder", TRACE)
** DB Specific logs:
*** /var/log/mysql
*** profileSQL=true&logger=com.mysql.jdbc.log.StandardLogger
* DB specific configuration might be necessary:
** For MySQL: rewriteBatchedStatements=true
* Batching won't work with Identity strategy!
* Additional options might be necessary for sorting SQL queries by Hibernate:
** <prop key="hibernate.order_updates">true</prop>
** <prop key="hibernate.order_inserts">true</prop>
