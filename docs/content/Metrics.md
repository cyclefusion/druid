---
layout: doc_page
---
# Druid Metrics

Druid generates metrics related to queries, ingestion, and coordination.

Metrics are emitted as JSON objects to a runtime log file or over HTTP (to a service such as Apache Kafka). Metric emission is disabled by default.

All Druid metrics share a common set of fields:

* `timestamp` - the time the metric was created
* `metric` - the name of the metric
* `service` - the service name that emitted the metric
* `host` - the host name that emitted the metric
* `value` - some numeric value associated with the metric

Each metric also has their own set of dimensions that vary from metric to metric.

Most metric values reset each emission period.

Available Metrics
-----------------

## Query Metrics

### Broker

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`query/time`|Milliseconds taken to complete a query.|< 1s|
|`query/node/time`|Milliseconds taken to query individual historical/realtime nodes.|< 1s|
|`query/intervalChunk/time`|Only emitted if interval chunking is enabled. Milliseconds required to query an interval chunk.|< 1s|

### Historical/Real-time

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`query/time`|Milliseconds taken to complete a query.|< 1s|
|`query/segment/time`|Milliseconds taken to query individual segment. Includes time to page in the segment from disk.|< several hundred milliseconds|
|`query/wait/time`|Milliseconds spent waiting for a segment to be scanned.|< several hundred milliseconds|
|`segment/scan/pending`|Number of segments in queue waiting to be scanned.|Close to 0|
|`query/segmentAndCache/time`|Milliseconds taken to query individual segment or hit the cache (if it is enabled on the historical node).|< several hundred milliseconds|

### Cache

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`query/cache/delta/*`|Cache metrics since the last emission.|N/A|
|`query/cache/total/*`|Total cache metrics.|N/A|


|Metric|Description|Normal Value|
|--------|-----------|-------|
|`*/numEntries`|Number of cache entries.|Varies.|
|`*/sizeBytes`|Size in bytes of cache entries.|Varies.|
|`*/hits`|Number of cache hits.|Varies.|
|`*/misses`|Number of cache misses.|Varies.|
|`*/evictions`|Number of cache evictions.|Varies.|
|`*/hitRate`|Cache hit rate.|~40%|
|`*/averageByte`|Average cache entry byte size.|Varies.|
|`*/timeouts`|Number of cache timeouts.|0|
|`*/errors`|Number of cache errors.|0|

## Ingestion Metrics

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`ingest/events/thrownAway`|Number of events rejected because they are outside the windowPeriod.|0|
|`ingest/events/unparseable`|Number of events rejected because the events are unparseable.|0|
|`ingest/events/processed`|Number of events successfully processed.|Equal to your # of events.|
|`ingest/rows/output`|Number of Druid rows persisted.|Your # of events with rollup.|
|`ingest/persists/count`|Number of times persist occurred.|Depends on configuration.|
|`ingest/persists/time`|Milliseconds spent doing intermediate persist.|Depends on configuration.|Generally a few minutes at most.|
|`ingest/persists/backPressure`|Number of persists pending.|0|
|`ingest/persists/failed`|Number of persists that failed.|0|
|`ingest/handoff/failed`|Number of handoffs that failed.|0|

### Indexing Service

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`task/run/time`|Milliseconds taken to run task.|Varies.|
|`segment/added/bytes`|Size in bytes of new segments created.|Varies.|
|`segment/moved/bytes`|Size in bytes of segments moved/archived via the Move Task.|Varies.|
|`segment/nuked/bytes`|Size in bytes of segments deleted via the Kill Task.|Varies.|

## Coordination

These metrics are for the Druid coordinator and are reset each time the coordinator runs the coordination logic.

The following metrics are emitted on a per tier basis:

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/*/added/count`|Number of segments added to the cluster.|Varies.|
|`segment/*/moved/count`|Number of segments moved in the cluster.|Varies.|
|`segment/*/dropped/count`|Number of segments dropped due to being overshadowed.|Varies.|
|`segment/*/deleted/count`|Number of segments dropped due to rules.|Varies.|
|`segment/*/cost/raw`|Used in cost balancing. The raw cost of hosting segments.|Varies.|
|`segment/*/cost/normalization`|Used in cost balancing. The normalization of hosting segments.|Varies.|
|`segment/*/cost/normalized`|Used in cost balancing. The normalized cost of hosting segments.|Varies.|

The following metrics are emitted for every historical node:

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/loadQueue/size`|Size in bytes of segments to load.|Varies.|
|`segment/loadQueue/failed`|Number of segments that failed to load.|0|
|`segment/loadQueue/count`|Number of segments to load.|Varies.|
|`segment/dropQueue/count`|Number of segments to drop.|Varies.|

The following metrics are emitted on a per datasource basis:

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/size`|Maximum byte limit available for segments.|Varies.|
|`segment/count`|Bytes used for served segments.|< max|

The following metrics are cluster wide:

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/overShadowed/count`|Number of overShadowed segments.|Varies.|

## General Health

### Historical

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/max`|Maximum byte limit available for segments.|Varies.|
|`segment/totalUsed`|Bytes used for served segments.|< max|
|`segment/totalUsedPercent`|Percentage of space used by served segments.|< 100%|
|`segment/totalCount`|Number of served segments.|Varies.|

On a per datasource level, the following metrics are also emitted.

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`segment/used`|Bytes used for served segments.|< max|
|`segment/usedPercent`|Percentage of space used by served segments.|< 100%|
|`segment/count`|Number of served segments.|Varies.|

### JVM

These metrics are only available if the JVMMonitor module is included.

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`jvm/pool/committed`|Committed pool.|close to max pool|
|`jvm/pool/init`|Initial pool.|Varies.|
|`jvm/pool/max`|Max pool.|Varies.|
|`jvm/pool/used`|Pool used.|< max pool|
|`jvm/bufferpool/count`|Bufferpool count.|Varies.|
|`jvm/bufferpool/used`|Bufferpool used.|close to capacity|
|`jvm/bufferpool/capacity`|Bufferpool capacity.|Varies.|
|`jvm/mem/init`|Initial memory.|Varies.|
|`jvm/mem/max`|Max memory.|Varies.|
|`jvm/mem/used`|Used memory.|< max memory|
|`jvm/mem/committed`|Committed memory.|close to max memory|
|`jvm/gc/count`|Garbage collection count.|< 100|
|`jvm/gc/time`|Garbage collection time.|< 1s|	

## Sys

These metrics are only available if the SysMonitor module is included.

|Metric|Description|Normal Value|
|--------|-----------|-------|
|`sys/swap/free`|Free swap.|Varies.|
|`sys/swap/max`|Max swap.|Varies.|
|`sys/swap/pageIn`|Paged in swap.|Varies.|
|`sys/swap/pageOut`|Paged out swap.|Varies.|
|`sys/disk/write/count`|Writes to disk.|Varies.|
|`sys/disk/read/count`|Reads from disk.|Varies.|
|`sys/disk/write/size`|Bytes written to disk. Can we used to determine how much paging is occuring with regards to segments.|Varies.|
|`sys/disk/read/size`|Bytes read from disk. Can we used to determine how much paging is occuring with regards to segments.|Varies.|
|`sys/net/write/size`|Bytes written to the network.|Varies.|
|`sys/net/read/size`|Bytes read from the network.|Varies.|
|`sys/fs/used`|Filesystem bytes used.|close to max pool|
|`sys/fs/max`|Filesystesm bytes max.|Varies.|
|`sys/mem/used`|Memory used.|close to max pool|
|`sys/mem/max`|Memory max.|Varies.|
