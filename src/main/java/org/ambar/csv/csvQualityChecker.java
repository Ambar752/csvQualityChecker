package org.ambar.csv;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.ambar.csv.commonUtils.ValidationUtils;
import org.ambar.csv.commonUtils.validationType;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class csvQualityChecker {
    private String csvPath;
    private Map<String,validationType> validationMetaData;

    public List<List<ValidationUtils.InvalidRecordDetails>> AllResult;
    public csvQualityChecker(String csvPath, Map<String,validationType> validationMetaData) {
        this.csvPath = csvPath;
        this.validationMetaData = validationMetaData;
    }

    public void runCheckers() {

        List<List<ValidationUtils.InvalidRecordDetails>> Result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(this.csvPath))) {
            String[] currRecord;
            int currRowPosition    = 0;
            int currColumnPosition = 0;

            Map<String,Integer> columnNamewisePositions = new LinkedHashMap<>();

            List<String> columnsToBeValidated  = new LinkedList<>();
            List<String>[] allDataOfAllcolumns = new LinkedList[this.validationMetaData.size()];

            while ((currRecord = reader.readNext()) != null) {

                if(currRowPosition == 0) {

                    for(Map.Entry<String,validationType> currValidationMetaData : this.validationMetaData.entrySet()) {
                        columnsToBeValidated.add(currValidationMetaData.getKey());
                    }

                    for (String value : currRecord) {
                            columnNamewisePositions.put(value,currColumnPosition);
                            currColumnPosition++;
                    }

                    currColumnPosition = 0;

                }

                if(currRowPosition != 0) {
                    for(int i = 0; i < this.validationMetaData.size(); i++) {
                        List<String> currColumnData = new LinkedList<>();
                        if(allDataOfAllcolumns[i] != null) {
                          currColumnData = allDataOfAllcolumns[i];
                        }
                        currColumnData.add(currRecord[columnNamewisePositions.get(columnsToBeValidated.get(i))]);
                        allDataOfAllcolumns[i] = currColumnData;
                    }
                }

                currRowPosition++;

            }

            for(int j=0; j < this.validationMetaData.size(); j++) {
                ValidationUtils.applyChecks(allDataOfAllcolumns[j],this.validationMetaData.get(columnsToBeValidated.get(j)));
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

//    public static void main(String[] args) {
//        String csvPath = "D:\\ambar\\IntelliJProjects\\csvQualityChecker\\resource\\data\\abc.csv";
//        Map<String,validationType> checkMetadata = new LinkedHashMap<>();
//        checkMetadata.put("Col1", validationType.UniqueCheck);
//        checkMetadata.put("Col3", validationType.NumericCheck);
//
//        csvQualityChecker cqHandle = new csvQualityChecker(csvPath,checkMetadata);
//        cqHandle.runCheckers();
//        cqHandle.printResults();
//        //List<List<ValidationUtils.InvalidRecordDetails>> Result =  cqHandle.AllResult;
//        //System.out.println(Result.size());
//    }
}
