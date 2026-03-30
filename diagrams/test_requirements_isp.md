# ISP Test Requirements for 5.2 Manual Baseline

## Scope
Selected methods:
1. DateTime.plusDays(int)
2. LocalDate.plusMonths(int)
3. Interval.overlaps(ReadableInterval)

This file defines input space partitions, boundary values, and required test obligations.

## 1) DateTime.plusDays(int)

### Input Partitions
- P1: days = 0
- P2: days > 0 (no boundary crossing)
- P3: days < 0 (no boundary crossing)
- P4: days causing month/year boundary crossing
- P5: leap-year sensitive dates (Feb 28/29)

### Boundary Values
- days: -1, 0, +1
- date boundaries: end/start of month, end/start of year
- leap boundaries: 2020-02-28 -> 2020-02-29, 2021-02-28 -> 2021-03-01

### Required Tests
- TR-DP-1: Cover P1 and verify identity path returns same instance.
- TR-DP-2: Cover P2 with days=+1 and verify date increments.
- TR-DP-3: Cover P3 with days=-1 and verify date decrements.
- TR-DP-4: Cover P4 with year crossing (Dec -> Jan).
- TR-DP-5: Cover P5 leap and non-leap transitions.

## 2) LocalDate.plusMonths(int)

### Input Partitions
- P1: months = 0
- P2: months > 0 (same-year movement)
- P3: months < 0 (same-year movement)
- P4: months causing year boundary crossing
- P5: month-end clamping (day 31 to shorter month)

### Boundary Values
- months: -1, 0, +1
- year boundary: Nov/Dec around +/- months
- clamp boundaries: Jan 31 + 1 in leap and non-leap years

### Required Tests
- TR-LM-1: Cover P1 and verify identity path returns same instance.
- TR-LM-2: Cover P2 with months=+1.
- TR-LM-3: Cover P3 with months=-1.
- TR-LM-4: Cover P4 with forward and backward year crossing.
- TR-LM-5: Cover P5 clamping to Feb 28 (non-leap) and Feb 29 (leap).

## 3) Interval.overlaps(ReadableInterval)

### Input Partitions
- P1: interval is null
- P2: interval is non-null and overlapping
- P3: interval is non-null and abutting (no overlap)
- P4: interval is non-null and disjoint (no overlap)

### Predicate Clauses
- Null branch predicate: thisStart < now AND now < thisEnd
- Non-null predicate: thisStart < otherEnd AND otherStart < thisEnd

### Boundary Values
- Equality boundaries for abutting:
  - thisEnd = otherStart
  - thisStart = otherEnd
- Null branch boundaries around now:
  - now exactly at start/end (false)
  - now strictly inside (true)

### Required Tests
- TR-IO-1: Null branch true outcome.
- TR-IO-2: Null branch false outcome.
- TR-IO-3: Non-null TT clause combination (true).
- TR-IO-4: Non-null TF clause combination (false).
- TR-IO-5: Non-null FT clause combination (false).
- TR-IO-6: Non-null disjoint false case.

## Traceability to Manual Baseline Tests
Implemented in:
- src/test/java/org/joda/time/LLMComponentManualBaselineTest.java

Relevant criteria covered:
- ISP partitions and boundaries
- Decision coverage on method guards
- Clause-oriented logic coverage for overlaps predicates
- Basic DFG def-use paths for method parameters to decision/computation/return
