# ROW vs COlUMN-ORIENTED (COLUMN-STORE / COLUMNAR) DB
## Row oriented
1. Tables are stored as rows in disk
2. Single block IO read to the table fetches multiple rows with all their columns
3. More IOs are required to find a particular row in a table scan but once the row is found, all columns for that row are also found. 
   
## Column oriented
1. Tables are stored as columns-first in disk
2. A single block IO read to the table fetches multiple columns with all the matching rows.
3. Less IO are required to get more values of a given column. But working with multiple columns require more IOs.
   
Sample `Employee` table:
   
| **Row_id** | **id** | **fname** | **lname** | **ssn** | **sal** | **title** | **dob**  | **joined** |
|------------|--------|-----------|-----------|---------|---------|-----------|----------|------------|
| 1001       | 1      | A         | AA        | 11      | 101     | AAA       | 1-2-2002 | 02-05-2023 |
| 1002       | 2      | B         | BB        | 22      | 102     | BBB       | 1-2-2002 | 02-05-2023 |
| 1003       | 3      | C         | CC        | 33      | 103     | CCC       | 1-2-2002 | 02-05-2023 |
| 1004       | 4      | D         | DD        | 44      | 104     | DDD       | 1-2-2002 | 02-05-2023 |
| 1005       | 5      | E         | EE        | 55      | 105     | EEE       | 1-2-2002 | 02-05-2023 |
| 1006       | 6      | F         | FF        | 66      | 106     | FFF       | 1-2-2002 | 02-05-2023 |
| 1007       | 7      | G         | GG        | 77      | 107     | GGG       | 1-2-2002 | 02-05-2023 |

**Storage representation in Row Oriented DB: (Assuming each page fits two rows)** </br>
**Page 1** -1001, 1, A, AA, 11, 101, AAA, 1-2-2002, 02-05-2023 ||| 1002, 2, B, BB, 22, 102, BBB, 1-2-2002, 02-05-2023 </br>
**Page 2** -1003, 3, C, CC, 333, 103, CCC, 1-2-2002, 02-05-2023 ||| 1004, 4, D, DD, 44, 104, DDD, 1-2-2002, 02-05-2023 </br>
and goes on

**Storage representation in Colummnar DB:** </br>
**Page 1** - 1:1001, 2:1002, 3:1003, 4:1004, 5:1005, 6:1006, 7:1007 .... </br>

**Page 2** - A:1001, B:1002, C:1003, D:1004 </br>
**Page 3** - E:1005, F:1006, G:1007 ... </br>

**Page 4** - AA:1001, BB:1002, CC:1003, DD:1004 </br>
**Page 5** - EE:1005, FF:1006, GG:1007 ... </br>

Now that you know how the data is stored in each of the DB types, think of the following queries and find out the expected number of page retrievals for each query with respect to both the types of DBs (assuming no indices) </br>
```sql 
1. SELECT fname FROM employee WHERE ssn = 66;
2. SELECT * FROM employee WHERE id = 1;
3. Select SUM(sal) from employee;
```

## Differences between Row vs Columnar DBs
| Row Based                         | Columnar |
|-----------------------------------|----------|
|Optimal for r/w                    | Writes are slower |
|OLTP                               | OLAP |
|Compression isn't efficient        | Compress greatly |
|Aggregation isn't efficient        | Amazing for aggregation |
| Efficient queries w/multi-columns | Inefficient queries w/multi-columns |

