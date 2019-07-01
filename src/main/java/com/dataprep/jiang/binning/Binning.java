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

    abstract void createBins(Collection<Object> data);

    protected class Bin {

        private int count = 0;

        private Object lowerBound;

        private Object higherBound;

        public Bin(Object lowerBound, Object higherBound) {
            this.lowerBound = lowerBound;
            this.higherBound = higherBound;
        }

        public int getCount() {
            return count;
        }

        public Object getLowerBound() {
            return lowerBound;
        }

        public Object getHigherBound() {
            return higherBound;
        }
    }

    protected class NumericBin extends Bin {

        public NumericBin(float lowerBound, float higherBound) {
            super(lowerBound, higherBound);
        }
    }

    protected class NominalBin extends Bin {

        public NominalBin(String lowerBound, String higherBound) {
            super(lowerBound, higherBound);
        }
    }
}
