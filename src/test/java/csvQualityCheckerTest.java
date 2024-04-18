import org.ambar.csv.commonUtils.ValidationUtils;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.ambar.csv.csvQualityChecker;
import org.ambar.csv.commonUtils.validationType;
import static org.mockito.Mockito.*;
public class csvQualityCheckerTest {

    private String csvpath;
    private String csvcleanpath;
    private Map<String,validationType> checkMetadata = new LinkedHashMap<>();

    @Before
    public void setUp() {
        csvpath = "D:\\ambar\\IntelliJProjects\\csvQualityChecker\\src\\test\\resources\\testdata\\myteststub.csv";
        csvcleanpath = "D:\\ambar\\IntelliJProjects\\csvQualityChecker\\src\\test\\resources\\testdata\\mytestcleanstub.csv";
        checkMetadata.put("Col2",validationType.UniqueCheck);
        checkMetadata.put("Col4",validationType.NumericCheck);
    }


    @Test
    public void testrunCheckersOnErrorFile() {
        csvQualityChecker mockedHandle = mock(csvQualityChecker.class);

        List<List<ValidationUtils.InvalidRecordDetails>> result = new ArrayList<>();
        csvQualityChecker csvCheckHandle = new csvQualityChecker(csvpath,checkMetadata);
        csvCheckHandle.runCheckers();
        result = csvCheckHandle.AllResult;

        assertEquals(result.get(0).get(0).rowNumber.intValue(),3);
        assertEquals(result.get(0).get(0).columnValue,"C");
        assertEquals(result.get(0).get(1).rowNumber.intValue(),4);
        assertEquals(result.get(0).get(1).columnValue,"C");
        assertEquals(result.get(1).get(0).rowNumber.intValue(),4);
        assertEquals(result.get(1).get(0).columnValue,"G");

        mockedHandle.printResults();
        csvCheckHandle.printResults();
        verify(mockedHandle).printResults();
    }

    @Test
    public void testrunCheckersOnCleanFile() {
        csvQualityChecker mockedHandle = mock(csvQualityChecker.class);

        List<List<ValidationUtils.InvalidRecordDetails>> result = new ArrayList<>();
        csvQualityChecker csvCheckHandle = new csvQualityChecker(csvcleanpath,checkMetadata);
        csvCheckHandle.runCheckers();
        result = csvCheckHandle.AllResult;

        assertEquals(result.get(0).size(),0);
        assertEquals(result.get(1).size(),0);

        mockedHandle.printResults();
        csvCheckHandle.printResults();
        verify(mockedHandle).printResults();

    }

}
