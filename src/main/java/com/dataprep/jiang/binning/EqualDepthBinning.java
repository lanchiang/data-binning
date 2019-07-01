package com.dataprep.jiang.binning;

import com.dataprep.jiang.datatype.DataTypeSniffer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lan Jiang
 */
public class EqualDepthBinning extends Binning {

    private final static float eps = 1;

    public EqualDepthBinning(Collection<?> data, int numOfBins) {
        super(data, numOfBins);
    }

    @Override
    protected void createBins() {
        dataType = DataTypeSniffer.sniffDataType(data);

        float dataSize = data.size();
        if (dataType == DataTypeSniffer.DataType.Numeric) {
            List<Double> f_data = data.stream().map(tuple -> Double.parseDouble(tuple.toString())).collect(Collectors.toList());
            Collections.sort(f_data);
            double base = f_data.get(0);
            double width = (f_data.get(f_data.size() - 1) - f_data.get(0)) / (double) numOfBins;
            for (int i = 0; i < numOfBins; i++) {
                double lowerBound = i * width + base;
                double higherBound;
                if (i == numOfBins - 1) {
                    higherBound = (i + 1) * width + base + eps;
                } else {
                    higherBound = (i + 1) * width + base;
                }
                long count = f_data.stream().filter(tuple -> tuple >= lowerBound && tuple < higherBound).count();
                Bin bin = new NumericBin(lowerBound, higherBound);
                bin.setCount(count);
                bins[i] = bin;
            }
            f_data.clear();
        } else {
            List<String> strings = data.stream().map(Object::toString).collect(Collectors.toList());
            List<String> distinctStrings = strings.stream().distinct().collect(Collectors.toList());
            Collections.sort(distinctStrings);
            double width = (double) distinctStrings.size() / (double) numOfBins;
            for (int i = 0; i < numOfBins; i++) {
                int lower = (int) (width * i);
                int higher = (int) (width * (i + 1));

                Bin bin;
                if (i == numOfBins - 1) {
                    bin = new TextBin(distinctStrings.get(lower), distinctStrings.get(higher - 1) + " ");
                } else {
                    bin = new TextBin(distinctStrings.get(lower), distinctStrings.get(higher));
                }
                long count = strings.stream()
                        .filter(tuple -> tuple.compareTo(bin.getLowerBound().toString()) >= 0 && tuple.compareTo(bin.getHigherBound().toString()) < 0)
                        .count();
                bin.setCount(count);
                bins[i] = bin;
            }
            distinctStrings.clear();
            strings.clear();
        }
    }
}
