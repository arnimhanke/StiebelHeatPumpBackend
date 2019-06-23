package de.hank.arnim;

import java.util.*;

/**
 * Created by arnim on 5/23/18.
 */
public class Cache {

    private Map<String, SortedMap<Long, ValueDto>> cache = new HashMap<>();
    private Map<String, List<Pair<Long, Long>>> savedIntervals = new HashMap<>();

    public void putValueInMap(String identifier, ValueDto value, long from, long to) {
        if (!cache.containsKey(identifier)) {
            cache.put(identifier, new TreeMap<>());
            savedIntervals.put(identifier, new ArrayList<>());
        }
        cache.get(identifier).put(value.getDate(), value);
        savedIntervals.get(identifier).add(new Pair<>(from, to));
    }

    public CacheResult getValueDtosInMap(String identifier, long from, long to) {
        List<ValueDto> values = new LinkedList<>();
        List<Pair<Long, Long>> notFoundIntervals = new ArrayList<>();

        SortedMap<Long, ValueDto> longValueDtoSortedMap = cache.get(identifier);
        if (longValueDtoSortedMap != null) {
            List<Long> longs = new ArrayList<>();
            longValueDtoSortedMap.keySet().forEach(aLong -> {
                if (aLong >= from || aLong <= to) {
                    longs.add(aLong);
                }
            });
            longs.forEach(aLong -> {
                values.add(longValueDtoSortedMap.get(aLong));
            });
            final long[] lastFoundStart = {from};
            final Pair[] lastPair = new Pair[]{new Pair<Long, Long>(0l, 0l)};
            savedIntervals.get(identifier).forEach(longLongPair -> {
                if (longLongPair.getObjA() > lastFoundStart[0]) {
                    notFoundIntervals.add(new Pair<>(lastFoundStart[0], longLongPair.getObjA()));
                    lastFoundStart[0] = longLongPair.getObjB();
                }
                lastPair[0] = longLongPair;
            });
            Pair<Long, Long> pair = lastPair[0];
            if (pair != null && pair.getObjB() < to) {
                notFoundIntervals.add(new Pair<>(pair.getObjB(), to));
            }
        } else {
            notFoundIntervals.add(new Pair<>(from, to));
        }

        return new CacheResult(values, notFoundIntervals);
    }

    public class Pair<A, B> {
        private A objA;
        private B objB;

        public Pair(A objA, B objB) {
            this.objA = objA;
            this.objB = objB;
        }

        public A getObjA() {
            return objA;
        }

        public void setObjA(A objA) {
            this.objA = objA;
        }

        public B getObjB() {
            return objB;
        }

        public void setObjB(B objB) {
            this.objB = objB;
        }
    }

    public class CacheResult {
        private List<ValueDto> foundValues;
        private List<Pair<Long, Long>> missingIntervals;

        public CacheResult(List<ValueDto> foundValues, List<Pair<Long, Long>> missingIntervals) {
            this.foundValues = foundValues;
            this.missingIntervals = missingIntervals;
        }

        public List<ValueDto> getFoundValues() {
            return foundValues;
        }

        public void setFoundValues(List<ValueDto> foundValues) {
            this.foundValues = foundValues;
        }

        public List<Pair<Long, Long>> getMissingIntervals() {
            return missingIntervals;
        }

        public void setMissingIntervals(List<Pair<Long, Long>> missingIntervals) {
            this.missingIntervals = missingIntervals;
        }
    }

}
