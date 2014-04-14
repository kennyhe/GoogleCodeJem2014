MineSweeperMaster
=================

Below is my solution, but it only solves the problem. I hope that I can see some good solutions which uses some dynamic programming or other algorithms (except brute force) to try to put the 0 cells into the board.

Below are steps of my algorithm:


     set remains = r * c - m.
1) m = 0: all are free spaces. print "." in all cells and then 
overwrite any cell with "c".

2) remains = 1: the same idea, . print "*" in all cells and then overwrite 
any cell with "c".

3) if r==1, or c==1: the solution is put "*" in first m cells, and "c" in the
 last cell, other cells are "."

4) if r==2, or c==2:
if m % 2==0, then have solution: in first m/2 columns or 
rows are "*", and "c" in the last cell, other cells are "." 
if m % 2 == 1, "Impossible".

5) in other cases, if remains is 2, 3, 5, 7, then "Impossible". else:

6) Fill the mines from (0,0), line by line, from left to right, up to down.
Need to consider the special cases:
    set rs = m / c; cs = m % c
6.1) if rs < r - 2,  and cs < c - 1, then well filled, and put "c" in last cell,
 other cells are "."
<pre>
**********
**********
**********
**********
****......
..........
..........
</pre>
6.2) if rs < r -3, and cs == c -1, then move one mine to the start of next line.
(This case)
<pre>
**********
**********
**********
*********.(not work here)
..........
..........
..........
(Final solution)
**********
**********
**********
********..(Move a * to the next row and put a space here)
*.........
..........
.........c
</pre>
6.3) if rs == r - 3, and cs == c -1, then move two mines to the start of two lines
<pre>
**********
**********
**********
*********.(not work here)
..........
..........
(Final solution)
**********
**********
**********
*******...(Move a * to the next row and put a space here)
*.........
*........c
</pre>
6.4) if rs >= r - 2, and remains % 2 == 0 then leave the first remains/2 
columns of the first two rows empty and fill the other cells with mines:
<pre>
c...******
....******
**********
**********
</pre>
6.5) if rs >= r - 2, and remains % 2 == 1, then leave the 
first (remains -3)/2 columns of the first two rows empty,
 and the first three columns of the 3rd row empty, and fill 
the other cells with mines:
<pre>
c...******
....******
...*******
**********
</pre>

Now all the conditions can be covered.


I am new to this system. Hope to get some credits if you really like my solution. And welcome to post your solutions. Thank you so much!
