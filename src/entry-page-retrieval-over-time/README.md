# Retrieval Experiments over time

Original topics build without taking our extracted anchor text into account. While this is necessary to failry compare retrieval by anchor text to other approaches, this comes with disadvantages for comparing the impact of the crawling time. Example: anchor text pointing to document not crawled in a snapshot but in another one or xY.
To evaluate the retrieval effectiveness over the multiple years we go to reduce the impact of the crawling strategies of the different common crawl snapshots by using the intersection of all common crawls and ommitting runs that have no , xy to.
With this setup, we can evaluate xy more fairly.

```
VERSION=v1 ./run-indexing-with-intersection-list.sh
VERSION=v2 ./run-indexing-with-intersection-list.sh
```

