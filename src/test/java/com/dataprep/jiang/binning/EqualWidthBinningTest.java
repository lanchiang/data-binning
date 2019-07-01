package com.dataprep.jiang.binning;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Lan Jiang
 */
public class EqualWidthBinningTest {

    @Test
    public void numericsEqualWidthBinningTest() {
        Double[] dataArray = new Double[]{0.5, 0.1, 0.3, 0.6, 1.3, 3.3, 2.3, 6.0, 4.5};
        Collection<?> data = Arrays.asList(dataArray);

        Binning binning = new EqualWidthBinning(2);
        binning.createBins(data);
    }
}
