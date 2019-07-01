package com.dataprep.jiang.binning;

import com.dataprep.jiang.datatype.DataTypeSniffer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lan Jiang
 */
abstract public class Binning {

    final int numOfBins;

    final Bin[] bins;

    final Collection<?> data;

    DataTypeSniffer.DataType dataType;

    Binning(Collection<?> data, int numOfBins) {
        this.data = data;
        this.numOfBins = numOfBins;
        bins = new Bin[this.numOfBins];
    }

    abstract protected void createBins();

    public void assignData() {
        if (Arrays.stream(bins).anyMatch(Objects::isNull)) {
            createBins();
        }

        if (dataType == DataTypeSniffer.DataType.Numeric) {
            int binCursor = 0;

            Queue<Double> dataQueue = data.stream()
                    .map(element -> Double.parseDouble(element.toString()))
                    .sorted().collect(Collectors.toCollection(LinkedList::new));

            Collection<Object> dataPoints = new LinkedList<>();
            while (!dataQueue.isEmpty()) {
                double dataPoint = dataQueue.peek();
                if (dataPoint < Double.parseDouble(bins[binCursor].getHigherBound().toString())) {
                    dataPoints.add(dataQueue.poll());
                } else {
                    bins[binCursor].fillBin(dataPoints);
                    dataPoints = new LinkedList<>();
                    binCursor++;
                }
            }
            bins[binCursor].fillBin(dataPoints);
        } else if (dataType == DataTypeSniffer.DataType.Text) {
            List<String> convertedData = data.stream().map(Object::toString).sorted().collect(Collectors.toList());
            for (String tuple : convertedData) {
                for (Bin bin : bins) {
                    if (tuple.compareTo(bin.getHigherBound().toString()) < 0) {
                        bin.setCount(bin.getCount() + 1);
                        break;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Data type does not exist.");
        }
    }

    protected class Bin {

        private long count = 0;

        private Object lowerBound;

        private Object higherBound;

        private Collection<Object> data;

        public Bin(Object lowerBound, Object higherBound) {
            this.lowerBound = lowerBound;
            this.higherBound = higherBound;
        }

        public Collection<Object> getData() {
            return data;
        }

        public void fillBin(Collection<Object> data) {
            this.data = data;
            this.count = data.size();
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Object getLowerBound() {
            return lowerBound;
        }

        public Object getHigherBound() {
            return higherBound;
        }
    }

    class NumericBin extends Bin {

        NumericBin(double lowerBound, double higherBound) {
            super(lowerBound, higherBound);
        }
    }

    class TextBin extends Bin {

        TextBin(String lowerBound, String higherBound) {
            super(lowerBound, higherBound);
        }
    }
}
