# ISP Test Requirements for 5.2 Manual Baseline

## Scope
Selected class: Interval

Selected methods:
1. Interval.overlap(ReadableInterval)
2. Interval.gap(ReadableInterval)
3. Interval.abuts(ReadableInterval)

This file defines input space partitions, boundary values, and required test obligations.

## 1) Interval.overlap(ReadableInterval)

### Input Partitions
- P1: input interval overlaps this interval
- P2: input interval abuts this interval (boundary touch, no overlap)
- P3: input interval is disjoint from this interval
- P4: input interval is null (interpreted as now-now interval)

### Boundary Values
- overlap boundaries: start = max(thisStart, otherStart), end = min(thisEnd, otherEnd)
- abut boundaries: thisEnd = otherStart or thisStart = otherEnd
- null-now boundary: now inside vs outside interval

### Required Tests
- TR-OV-1: Overlap case returns non-null intersection with expected start/end.
- TR-OV-2: Abutting case returns null.
- TR-OV-3: Disjoint case returns null.
- TR-OV-4: Null-input case with now inside returns zero-length interval.

## 2) Interval.gap(ReadableInterval)

### Input Partitions
- P1: thisStart > otherEnd (other interval fully before)
- P2: otherStart > thisEnd (other interval fully after)
- P3: intervals overlap
- P4: intervals abut
- P5: input interval is null

### Boundary Values
- decision boundary 1: thisStart == otherEnd (not a gap)
- decision boundary 2: otherStart == thisEnd (not a gap)
- gap bounds: [otherEnd, thisStart] and [thisEnd, otherStart]

### Required Tests
- TR-GP-1: thisStart > otherEnd path returns expected gap.
- TR-GP-2: otherStart > thisEnd path returns expected gap.
- TR-GP-3: overlap path returns null.
- TR-GP-4: abut path returns null.

## 3) Interval.abuts(ReadableInterval)

### Input Partitions
- P1: interval is null
- P2: non-null left-abutting (otherEnd == thisStart)
- P3: non-null right-abutting (thisEnd == otherStart)
- P4: non-null overlapping
- P5: non-null disjoint

### Predicate Clauses
- Null branch predicate: thisStart == now OR thisEnd == now
- Non-null predicate: otherEnd == thisStart OR thisEnd == otherStart

### Boundary Values
- non-null equality boundaries: both abut equalities above
- null boundary: now not matching either endpoint (false)

### Required Tests
- TR-AB-1: Left-abut true (first clause true).
- TR-AB-2: Right-abut true (second clause true).
- TR-AB-3: Overlap false.
- TR-AB-4: Disjoint false.
- TR-AB-5: Null-input false when now is outside endpoints.

## Traceability to Manual Baseline Tests
Implemented in:
- src/test/java/org/joda/time/LLMComponentManualBaselineTest.java

Relevant criteria covered:
- ISP partitions and boundaries
- Decision coverage on method branches and if/else-if chains
- Clause-oriented logic coverage for abuts and overlap-related predicates
- Basic DFG def-use paths for interval boundaries and computed return values
