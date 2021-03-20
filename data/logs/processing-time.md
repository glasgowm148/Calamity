# Tracking of completion time with various config settings

* a = lines per actor
* e = Embeddings
* see below | ++heap



100a50e: 1hr+
50a50e:38-34m
25a50e:  | ++heap
10a50e: 38m
1a50e: 1hr+ | ++heap


100a300e:
50a300e:
10a300e:
1a300e:


## | ++heap

Too many actors causes Heap overload / Garbage Cleaning
```bash
[warn] In the last 10 seconds, 8.364 (86.1%) were spent in GC. [Heap: 8.68GB free of 11.73GB, max 11.89GB] Consider increasing the JVM heap using `-Xmx` or try a different collector, e.g. `-XX:+UseG1GC`, for better performance.
```