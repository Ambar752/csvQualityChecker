import org.ambar.csv.commonUtils.ValidationUtils;
import org.ambar.csv.commonUtils.validationType;
import org.junit.*;
import static  org.junit.Assert.assertEquals;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationUtilsTest {

    private static List<String> uniqueList;
    private static List<String> nonUniqueList;
    private static List<String> allNumeric;
    private static List<String> someNonNumeric;
    private static validationType uniqueChecker;
    private static validationType numericChecker;

    @BeforeClass
    public static void setUpClass() {
        ValidationUtilsTest.uniqueList     = new ArrayList<>(Arrays.asList("1","2","3","4"));
        ValidationUtilsTest.nonUniqueList  = new ArrayList<>(Arrays.asList("A","B","B","C"));
        ValidationUtilsTest.allNumeric     = new ArrayList<>(Arrays.asList("5","5","6","7"));
        ValidationUtilsTest.someNonNumeric = new ArrayList<>(Arrays.asList("5","A","6","7"));
        ValidationUtilsTest.uniqueChecker  = validationType.UniqueCheck;
        ValidationUtilsTest.numericChecker = validationType.NumericCheck;
    }

    @Test
    public void testapplyUniqueChecks() {
        List<ValidationUtils.InvalidRecordDetails> currentResult = new ArrayList<>();
        ValidationUtils.applyChecks(uniqueList,uniqueChecker);
        currentResult = ValidationUtils.Result;
        assertEquals(0, currentResult.size());

        ValidationUtils.applyChecks(nonUniqueList,uniqueChecker);
        currentResult = ValidationUtils.Result;
        assertEquals(2, currentResult.size());
        assertEquals(3, currentResult.get(0).rowNumber.intValue());
        assertEquals("B", currentResult.get(0).columnValue);

        assertEquals(4, currentResult.get(1).rowNumber.intValue());
        assertEquals("B", currentResult.get(1).columnValue);
    }

    @Test
    public void testapplyNumericChecks() {
        List<ValidationUtils.InvalidRecordDetails> currentResult = new ArrayList<>();
        ValidationUtils.applyChecks(allNumeric,numericChecker);
        currentResult = ValidationUtils.Result;
        assertEquals(0, currentResult.size());

        ValidationUtils.applyChecks(someNonNumeric,numericChecker);
        currentResult = ValidationUtils.Result;

        assertEquals(1, currentResult.size());
        assertEquals(3, currentResult.get(0).rowNumber.intValue());
        assertEquals("A", currentResult.get(0).columnValue);

    }
}
