## TRANSACTIONS
Collection of queries (one unit of work) </br>
` Transaction BEGIN ` </br>
`Transaction COMMIT` </br>
`Transaction ROLLBACK` </br>
PostgreSQL - Commits faster (lot of I/O) </br>
SQL Server - Commits are slower for large query </br>
Nature - Used to change or modify data. Perfectly normal to have read-only transactions. </br>
## ACID
Transaction Properties - **Atomicity, Consistency, Isolation, Durability**

###  1. ATOMICITY
1. All queries in a transaction must succeed
2. If any one query fails, all prior successful queries in the transaction should rollback
3. If the DB goes down prior to commit of a transaction, all the successful queries should be rolled-back

###  2. CONSISTENCY
#### 1. Consistency in data
1. Defined by user
2. Referential Integrity (foreign keys)
3. Atomicity
4. Isolation

#### 2. Consistency in reads
* If a transaction committed a change, will a new transaction immediately see the new change?
* Affects the system as a whole
* RDB and NRDB suffer from this
* Eventual Consistency

###  3. ISOLATION
Can my inflight transaction see changes made by other transactions? </br>
*Isolation Read Phenomenons (Undesirable)* 
1. **Dirty Reads** (reading something that written but not full flushed yet) </br>
2. **Non-Repeatable reads** (reading same thing two times but getting different values each time in a single transaction - *(not repeatable in my own transaction)*) </br>
3. **Phantom Reads** - can't catch a particular write in a transaction </br>
4. **Lost Updates** -   (write something, but before committing, read that something, but it is lost because some other transaction over-wrote it) </br>
####  1. DIRTY READS
Reading something that's not even committed yet </br>
![Dirty reads](../Assets/Dirty-reads-(182).png)
####  2. NON-REPEATABLE READS
Inconsistent read </br>
![Non-repeatable reads](../Aszets/Non-repeatable-read-(183).png)
####  3. PHANTOM READS
Not read before, read now </br>
![Phantom reads](../Assets/Phantom-reads-(184).png)
####  4. LOST UPDATES
One update overwritten by another transaction, resulting in loss of update  </br>
![Phantom reads](../Assets/lost-updates-(185).png)
#### ISOLATION LEVELS for inflight transaction </br>
> **Read uncommitted:** No isolation,any change from outside is visible even if the're uncommitted </br>
> **Read committed:** Each query in a transaction sees only committed changes by other transaction </br>
> **Repeatale read:**  The transaction will make sure that when a query reads a row, that row will remain unchanged during the transaction. </br> 
> **Snapshot:**  Each query in a transaction sees only changes committed up to the start of the transaction. It's like a snapshot version of the db at the moment. </br> 
> **Serializable:**  Transactions are run as if they're serialized one after the other. </br>
> ![Read phenomena vs Isolation level](../Assets\Read-phenomena-vs-Isolation-level-(186).png)

#### DB IMPL of ISOLATION</br>
> Each db implements isolation differently. </br>
> **Optimistic:** No locks, just track if things changed, and fail the transaction if so </br>
> **Pessimistic:** Row level locks, table locks, page locks to avoid lost updates </br>
> **Repeatale read** "locks" the rows it reads but it could be expensive if you read a lot of rows, PostGres implements RR as snapshots. That's why no Phantom reads with postgres in repeatable read </br>
> **Serializable** are usually implemented with optimistic concurrency control, you can implement it pessimistically with `SELECT FOR UPDATE` 

## 4. Durability
* Changes made by committed transactions must be persisted in a durable non-volatile storage
*  Durability techniques -
   * WAL - Write Ahead log
        > * Writing a lot of data to disk is expensive (indexes, files, columns, rows, etc.)
        > * That's why DBMSs persist a compressed version of the changes known as WAL segments
   * Asynchronous snapshot
   * Append only file (REDIS chache)
* A write request in OS usually goes to the OS cache (OS batches these and flush to disk, to reduce IO)
* When the writes goes to the OS cache, an OS crash could lead to loss of cache data
* `fsync` command forces writes to always go to disk
* `fsync `can be expensive and slow down commits       