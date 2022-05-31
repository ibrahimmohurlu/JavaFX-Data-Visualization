package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    private File dataFile;
    private String title, xlabel;
    //arraylist to hold raw(unordered) data
    private ArrayList<Record> recordList;
    //hashmap to hold data ordered by time period
    private HashMap<LocalDate, ArrayList<Record>> data;
    private String dataFileExtension;


    Parser(File f) {
        this.dataFile = f;
        if (dataFile != null) {
            //getting the file extension by looking after the last '.' character
            String fileName = dataFile.getName();
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                dataFileExtension = fileName.substring(i + 1);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getXlabel() {
        return xlabel;
    }

    public String getDataFileExtension() {
        return dataFileExtension;
    }

    public HashMap<LocalDate, ArrayList<Record>> getData() {
        return data;
    }

    private void processRawData(ArrayList<Record> list) {
        //constructing data variable before start
        this.data = new HashMap<>();


        for (int i = 0; i < list.size(); i++) {
            //arraylist that contains data grouped by year to add hashmap
            ArrayList<Record> groupedList = new ArrayList<>();

            Record currentRecord = list.get(i);
            String currentRecordYear = currentRecord.getVal("Year");
            groupedList.add(currentRecord);
            for (int j = i + 1; j < list.size(); j++) {
                Record nextRecord = list.get(j);
                String nextRecordYear = nextRecord.getVal("Year");
                //if year value is the same and groupedList is NOT contains the current element than add to list
                if (nextRecordYear.equals(currentRecordYear) && !groupedList.contains(nextRecord)) {
                    groupedList.add(nextRecord);
                }
            }

            String[] tokens = currentRecordYear.split("[./-]");
            LocalDate date;

            if (tokens.length == 1) {
                //we only have year so create a LocalDate as [Year]-1-1
                date = LocalDate.of(Integer.parseInt(tokens[0]), 1, 1);
            } else {
                date = LocalDate.parse(currentRecordYear);
            }
            //add groupedList to hashmap if its not containing same key and also check for null date and empty list
            if (date != null && !groupedList.isEmpty() && !this.data.containsKey(date)) {
                this.data.put(date, groupedList);
            }


        }


    }

    public void Parse() throws IOException, ParserConfigurationException, SAXException {
        if (this.dataFile != null && dataFileExtension != null) {
            //constructing array list to read raw data
            recordList = new ArrayList<>();
            if (dataFileExtension.equals("txt")) {
                //parsing txt
                Scanner sc = new Scanner(this.dataFile);
                this.title = sc.nextLine();
                this.xlabel = sc.nextLine();
                while (sc.hasNextLine()) {
                    int numberOfRecords = 0;
                    //starting of a new group
                    if (sc.nextLine() == "" && sc.hasNextLine()) {
                        //Trying to catch a group number to parse
                        String tryNumberOfRecords = sc.nextLine();
                        if (tryNumberOfRecords != "") {
                            numberOfRecords = Integer.parseInt(tryNumberOfRecords);
                            for (int i = 0; i < numberOfRecords; i++) {
                                String[] tokens = sc.nextLine().split("[,]");
                                recordList.add(new Record(tokens));
                            }
                        }
                    }
                }
            } else if (dataFileExtension.equals("xml")) {
                //parsing xml
                Document document;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                //Get Document Builder
                DocumentBuilder builder = factory.newDocumentBuilder();
                //Build Document
                document = builder.parse(dataFile);
                //it is good to normalize it says on the internet
                document.getDocumentElement().normalize();
                //Get all records
                NodeList recordNodeList = document.getElementsByTagName("record");
                title = document.getElementsByTagName("title").item(0).getTextContent();
                xlabel = document.getElementsByTagName("xlabel").item(0).getTextContent();

                for (int i = 0; i < recordNodeList.getLength(); i++) {
                    Node node = recordNodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        NodeList fields = element.getElementsByTagName("field");
                        Record record = new Record();
                        for (int j = 0; j < fields.getLength(); j++) {
                            Element e = (Element) fields.item(j);

                            record.setFields(e.getAttribute("name"), e.getTextContent());
                        }
                        recordList.add(record);
                    }

                }
            }
            //process raw data
            processRawData(recordList);

        }
    }
}
