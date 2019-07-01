package com.dataprep.jiang.binning;

import com.dataprep.jiang.datatype.DataTypeSniffer;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Lan Jiang
 */
public class EqualWidthBinning extends Binning {

    public EqualWidthBinning(int numOfBins) {
        super(numOfBins);
    }

    @Override
    void createBins(Collection<?> data) {
        double width = (double) data.size() / (double) numOfBins;
        Set<Object> distinctData = new TreeSet<>(data);

        DataTypeSniffer.DataType dataType = DataTypeSniffer.sniffDataType(distinctData);
        if (dataType == DataTypeSniffer.DataType.Numeric) {
            List<Double> numerics = distinctData.parallelStream().map(element -> Double.parseDouble(element.toString())).collect(Collectors.toList());
            for (int i = 0; i < numOfBins; i++) {
                int lowerBound = (int) (i * width);
                int higherBound = (int) ((i + 1) * width);
                if (i == numOfBins - 1) {
                    bins[i] = new NumericBin(numerics.get(lowerBound), numerics.get(higherBound - 1) + 1);
                } else {
                    bins[i] = new NumericBin(numerics.get(lowerBound), numerics.get(higherBound));
                }
            }
        } else if (dataType == DataTypeSniffer.DataType.Nominal) {
            List<String> strings = distinctData.stream().map(Object::toString).collect(Collectors.toList());
            for (int i=0;i<numOfBins;i++) {
                int lower = (int) (width*i);
                int higher = (int) (width*(i+1));
                if (i==numOfBins-1) {
                    bins[i] = new NominalBin(strings.get(lower), strings.get(higher - 1) + " ");
                } else {
                    bins[i] = new NominalBin(strings.get(lower), strings.get(higher));
                }
            }
        } else {
            throw new RuntimeException(new IllegalArgumentException("Data type is incorrect."));
        }
    }
}
