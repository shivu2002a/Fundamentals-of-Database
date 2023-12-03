# Storage concepts: 
1. Table
2. Row_id
3. Page
4. IO
5. Heap data structure
6. Index data structure b-tree
<!-- 7. Example of a query -->

## 1. Logical table
1. Provides tabular representation of all the rows

## 2. Row_id
1. Internal and system maintained
2. In certain DBs (MySQL-InnoDB) it is same as the primary key, but other DBs like Postgres have a system column row_id (tuple_id)

## 3. Page
1. Depending on the storage model (row vs columns-store), the rows are stored and read in logical pages
2. The DB doesn't read a row but one/more pages in a single IO and we get a lot of rows in that IO
3. Each page has a particular size (e.g 8KB in Postgres, 16KB in MySQL)
4. Assuming each page holds 3 rows, if 1001 rows No. of pages = 1001 / 3 = 333~ pages 

## 4. IO
1. IO operation (read/write) is a read request to the disk
2. We try to minimize this as much as possible
3. An IO can fetch 1 page or more depending on the disk partitions and other factors
4. An IO cannot read a single row, it reads one or more page each containing same no of rows
5. We have to minimize the number of IOs as they are expensive
6. Some IOs in OS goes to the OS cache and not disk 
   
## 5. Heap data structure
1. Heap is a data structure where the table is stored with all its pages one after another
2. This is where all the data is stored including everything
3. Traversing the heap is very expensive as we need to read so many data to find what we want
4. That is why we need **indices** that help tell us exactly what part of the heap we need to read. **What page(s) of the heap we need to pull**

## 6. Index
1. Index is another data structure separate from heap that has pointers to the heap
2. It has part of the data and is used to quickly search for something
3. You can index one or more column
4. Once you find a value of the index, you go to the heap to fetch more information where everything is there
5. Index tells you exactly which page to fetch in the heap instead of taking the hit to scan every page in the heap.
6. The index is also stored as pages and cost IO to pull the entries of the index
7.  The smaller the index, the more it can fit in memory, the faster the search
8.  Popular data structure for index is b-trees (learn more in b-tree section)

# Notes
1. Sometimes, the heap table can be organized around a single index. This is *clustered index* or and *Index Organized Table*
2. PK is a CI unless otherwise specified
3. *MySQL InnoDB* always have a PK, other index point to PK "value"
4. *PostGres* have secondary indexes and all indexes point directly to the row_id which lives in the heap. 
5. 
