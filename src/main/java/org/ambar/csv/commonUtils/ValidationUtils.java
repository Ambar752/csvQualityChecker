package org.ambar.csv.commonUtils;

import java.util.*;

public class ValidationUtils {

    public static class InvalidRecordDetails {
        public Integer rowNumber;
        public String columnValue;

        public String checkType;

        InvalidRecordDetails(Integer rowNumber, String columnValue, String checkType) {
            this.rowNumber = rowNumber;
            this.columnValue = columnValue;
            this.checkType = checkType;
        }

    }

    public static List<InvalidRecordDetails> Result;

    public static void applyChecks(List<String> columnData,validationType checkType) {

        List<InvalidRecordDetails> result = new ArrayList<InvalidRecordDetails>();

        if(checkType == validationType.UniqueCheck) {
            result = ValidationUtils.applyUniqueCheck(columnData);
        } else if (checkType == validationType.NumericCheck) {
            result = ValidationUtils.applyNumericCheck(columnData);
        }

        ValidationUtils.Result = result;
    }

    private static List<InvalidRecordDetails> applyUniqueCheck(List<String> columnData) {
        List<InvalidRecordDetails> result = new ArrayList<InvalidRecordDetails>();
        int rowNumber = 0;
        Map<String,Integer> trackingMap = new HashMap<>();
        for(String currColumnValue : columnData) {
            rowNumber++;
            if(!trackingMap.containsKey(currColumnValue)) {
                trackingMap.put(currColumnValue,rowNumber);
            } else {
                int oldrowNumber = trackingMap.get(currColumnValue);
                InvalidRecordDetails invalidrec = new InvalidRecordDetails(oldrowNumber+1, currColumnValue, validationType.UniqueCheck.toString());
                result.add(invalidrec);
                InvalidRecordDetails invalidrec1 = new InvalidRecordDetails(rowNumber+1, currColumnValue, validationType.UniqueCheck.toString());
                result.add(invalidrec1);
            }
        }
        return result;
    }

    private static List<InvalidRecordDetails> applyNumericCheck(List<String> columnData) {
        List<InvalidRecordDetails> result = new ArrayList<InvalidRecordDetails>();
        int rowNumber = 0;

        for(String currColumnValue : columnData) {
            rowNumber++;
            if(!currColumnValue.matches("\\d+")) {
                InvalidRecordDetails invalidrec = new InvalidRecordDetails(rowNumber+1, currColumnValue,validationType.NumericCheck.toString());
                result.add(invalidrec);
            }
        }
        return result;
    }

//    public static void main(String[] args) {
//        validationType vname = validationType.NumericCheck;
//        validationType vname1 = validationType.UniqueCheck;
//        List<String> mydata = new ArrayList<>(Arrays.asList("1","2","3","B"));
//        List<String> mydata1 = new ArrayList<>(Arrays.asList("A","B","A"));
//
//        ValidationUtils.applyChecks(mydata,vname);
//        System.out.println(ValidationUtils.Result.get(0).rowNumber);
//        System.out.println(ValidationUtils.Result.get(0).columnValue);
//
//        ValidationUtils.applyChecks(mydata1,vname1);
//        System.out.println(ValidationUtils.Result.get(0).rowNumber);
//        System.out.println(ValidationUtils.Result.get(0).columnValue);
//        System.out.println(ValidationUtils.Result.get(1).rowNumber);
//        System.out.println(ValidationUtils.Result.get(1).columnValue);
//
//    }
}
