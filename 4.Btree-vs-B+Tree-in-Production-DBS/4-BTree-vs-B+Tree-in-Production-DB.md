## Agenda
1. Full Table Scan
2. BTree
3. BTree Limitations
4. B+ Tree 
5. B+ Tree Considerations
6. B+ Tree Storage Cost in MySQL vs Postgres

## 1. FULL TABLE SCAN
* To find a row in a large table, we perform a full table scan
* Reading large table is slow
* Requires many IO to read all pages
* We need a way to reduce the IO

## 2. BTREE
* Balanced data structure for fast traversal
* B-Tree has nodes
* In BTree of 'm' degree, some nodes, can have 'm' child nodes
* Node has up to (m-1) nodes
* Each element is a key value pair, value is usually the data pointer to the row
* Data pointer can point to PK or tuple
* Root node, internal node, leaf node
* A node = disk page 
  
## 3. BTREE LIMITATIONS
* Elements in all nodes store both the key and the value
* Internal nodes take more space thus require more IO and can slow down traversal
* Range queries are slow because of random access
* Secondary indices become large
* B+ tree solves both the above problems

## 4. B+TREE 
* Exactly like Btree but only stores keys in internal nodes
* Values are only stored in leaf nodes
* Internal nodes are smaller since they only store the keys and they can fit more elements
* Leaf nodes are linked so once you find a key you can find all the values before and after that key
* Great for range queries
  
## 5. B+ Tree Considerations
* Cost of leaf pointer (cheap)
* 1 Node fits a DBMS page (most DBMS)
* Can fit internal nodes easily in memory for fast traversal
* Leaf nodes can live in data files in the heap
* Most DBMS systems use B+ tree
 
## 6. B+ Tree Storage Cost in MySQL vs Postgres
* B+ trees secondary index values can either point directly to tuple (Postgres) or to the PK (MySQL)
* If the PK data type is expensive this can cause bloat in all the secondary indices for DB such as MySQL (InnoDB)
* Leaf nodes in MySQL contains the full row since its an IOT (Index Organized table) / clustered index