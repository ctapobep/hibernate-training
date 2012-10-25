hibernate-training
==================
**M1 general overview of technologies (4h)**

*0. Course description*

During the course we're going to talk about Hibernate, but not only. Hibernate is an ORM tool that gives you an ability to develop your data access levels quickly, but it doesn't free you from knowing DBs. In fact in order to use Hibernate effectively you need to know databases pretty well. So besides Hibernate in scope of this training we'll also get acquainted with general database/JDBC theory:

*Transaction isolation & propagation*

- Strategies of PK generation 
- Different types of statements & DB caching 
- Concurrent work with DBs (different types of locking) 
- Working with hierarchical data structures 
- OO/Relational paradigms mismatch (associations and inheritance on the relational level) 
 
Also we'll take a look at Spring Tx & Spring Dao since that's a typical way of working with Hibernate.

*1. Working with DB is hard, you should know a lot to work with it*

- Understand and choose appropriate indexes 
- Transaction Isolation & propagation 
- DB & Concurrency (different types of locks) 
- Caches
- Connection Pools
- Error handling

*2. Difference between frameworks*

- JDBC (need to explain what Prepared Statement is) 
- Spring JDBC 
- iBatis/MyBatis 
- Hibernate

**M2 Objects, their states and residence (2h)**

*1. Object life cycle*

- States of objects 
- Session/Persistent Context/Unit Of Work 
- Proxies & laziness 
- get/load/update/merge

*2. Caches*

- Session level "cache" and how to write tests 
- Process level cache 
- Query cache & how different queries impact it 
- Distributed cache

**M3 Object graphs (3-4h)**

*Associations* 

- OTO, MTO, OTM 
- Bidirectionals 
- Cascades 
- Embedded objects 
- User Types 
- HQL & Criteria API; Pagination 
- Transformers, Views 
- Hierarchical structures and working with them (Materialized Path, Nested Sets)

*Identities*

- Different types of generators 
- Different strategies of ID generation

**M4 Fetching & Collections (2h)**

*Fetching*

- Fetch strategies (select, join, sub-select) 
- Lazy options (true, false, extra, etc.)

*Collections*

- Lists, Sets, Bags, Maps
- Getting Performance with fetch strategies & collections

**M5 Inheritance (2h)**

- Fowler Patterns & their implementation in Hibernate 
- Mixed patterns 
- Caveats in Hibernate

**M6 Locking (1.5h)**

**M7 Hibernate Usage (3h)**

- Hibernate & Transactions; Spring Tx & Spring DAO 
- Open Session in View 
- Open Session per Conversation 
- Web Services and other serialization

**M8 Hibernate Validator (1h)**

**M9 Annotations vs XML (2h)**

**M10 Performance Optimization (1h)**

**M11 Statistics & tools (1h)**