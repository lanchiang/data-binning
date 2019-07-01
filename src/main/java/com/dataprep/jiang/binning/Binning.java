package com.dataprep.jiang.binning;

import java.util.Collection;

/**
 *
 * @author Lan Jiang
 */
abstract public class Binning {

    protected final int numOfBins;

    protected final Bin[] bins;

    public Binning(int numOfBins) {
        this.numOfBins = numOfBins;
        bins = new Bin[this.numOfBins];
    }

    abstract void createBins(Collection<?> data);

    protected class Bin {

        private long count = 0;

        private Object lowerBound;

        private Object higherBound;

        public Bin(Object lowerBound, Object higherBound) {
            this.lowerBound = lowerBound;
            this.higherBound = higherBound;
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

    protected class NumericBin extends Bin {

        public NumericBin(double lowerBound, double higherBound) {
            super(lowerBound, higherBound);
        }
    }

    protected class TextBin extends Bin {

        public TextBin(String lowerBound, String higherBound) {
            super(lowerBound, higherBound);
        }
    }
}
