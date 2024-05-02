package org.ambar.csv;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.ambar.csv.commonUtils.ValidationUtils;
import org.ambar.csv.commonUtils.validationType;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import org.ambar.utils.CustomerLogFormatter;

public class csvQualityChecker {
    private String csvPath;
    private Map<String,validationType> validationMetaData;

    private List<String> columnNamesToBeValidated = new LinkedList<>();

    private Map<String,Integer> columnNamewisePositions = new LinkedHashMap<>();

    private Map<String,List<String>> columnDataStore = new HashMap<>();

    public List<List<ValidationUtils.InvalidRecordDetails>> AllResult;
    public csvQualityChecker(String csvPath, Map<String,validationType> validationMetaData) {
        this.csvPath = csvPath;
        this.validationMetaData = validationMetaData;
    }

    private void saveColumnPositions(String[] currentRow) {
        int currentColumnPosition = 0;
        for(String currentColumnValue : currentRow) {
            this.columnNamewisePositions.put(currentColumnValue,currentColumnPosition);
            currentColumnPosition++;
        }
    }
    private void setcolumnNamesToBeValidated(Map<String,validationType> validationMetaData) {
        for(Map.Entry<String,validationType> currValidationMetaData : validationMetaData.entrySet()) {
            this.columnNamesToBeValidated.add(currValidationMetaData.getKey());
        }
    }

    private void setcolumnDataStore(String[] currRecord, List<String> columnNamesToBeValidated) {
        for(String columnName : columnNamesToBeValidated) {
            List<String> currColumnData = new LinkedList<>();
            currColumnData = this.columnDataStore.getOrDefault(columnName,new LinkedList<>());
            currColumnData.add(currRecord[this.columnNamewisePositions.get(columnName)]);
            this.columnDataStore.put(columnName,currColumnData);
        }
    }

    public void runCheckers() {

        List<List<ValidationUtils.InvalidRecordDetails>> Result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(this.csvPath))) {

            String[] currRecord;
            int currRowPosition    = 0;

            List<String>[] allDataOfAllcolumns  = new LinkedList[this.validationMetaData.size()];

            while ((currRecord = reader.readNext()) != null) {

                if(currRowPosition == 0) {
                    saveColumnPositions(currRecord);
                    setcolumnNamesToBeValidated(this.validationMetaData);
                } else {
                    setcolumnDataStore(currRecord,this.columnNamesToBeValidated);
                }

                currRowPosition++;

            }

            for(Map.Entry<String,List<String>> allcolumnData : this.columnDataStore.entrySet()) {
                ValidationUtils.applyChecks(allcolumnData.getValue(),this.validationMetaData.get(allcolumnData.getKey()));
                Result.add(ValidationUtils.Result);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        this.AllResult =  Result;
    }

    public void printResults() {
        boolean foundAnyError = false;
        Map<String, List<String>> parsedInvalidRecords = new HashMap<>();

        for (List<ValidationUtils.InvalidRecordDetails> currResultRow : this.AllResult) {

            for (int i = 0; i < currResultRow.size(); i++) {
                if (currResultRow.get(i).checkType != "") {
                    foundAnyError = true;
                    List<String> currentRowNumberList = parsedInvalidRecords.getOrDefault(currResultRow.get(i).checkType, new ArrayList<>());
                    currentRowNumberList.add(currResultRow.get(i).rowNumber.toString());
                    parsedInvalidRecords.put(currResultRow.get(i).checkType, currentRowNumberList);
                }
            }
        }

        if (!foundAnyError) {
            System.out.println("CSV Parsed SuccessFully with No violation !!");
        } else {
            for (Map.Entry<String, List<String>> entry : parsedInvalidRecords.entrySet()) {
                String Row = "Contraint Type : ";
                Row = Row + entry.getKey() + " | ";
                Row = Row + "Affected Row Numbers are : " + String.join(",", entry.getValue()) + "\n";
                System.out.println(Row);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger("MyLogger");
        FileHandler fileHandler = new FileHandler("myLogFile.log",true);
        CustomerLogFormatter formatter = new CustomerLogFormatter(CustomerLogFormatter.format.DETAILED);
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);

        String csvPath = "D:\\ambar\\IntelliJProjects\\csvQualityChecker\\resource\\data\\abc.csv";
        Map<String,validationType> checkMetadata = new LinkedHashMap<>();
        checkMetadata.put("Col1", validationType.UniqueCheck);
        checkMetadata.put("Col3", validationType.NumericCheck);

        csvQualityChecker cqHandle = new csvQualityChecker(csvPath,checkMetadata);
        cqHandle.runCheckers();
        cqHandle.printResults();
        logger.info("Completed All Checks Try3");

        //List<List<ValidationUtils.InvalidRecordDetails>> Result =  cqHandle.AllResult;
        //System.out.println(Result.size());
    }
}
