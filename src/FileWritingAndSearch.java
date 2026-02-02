import java.io.*;
import java.util.*;

public class FileWritingAndSearch {
    public static File mergeFiles(String path1, String path2) throws IOException {
        /*This method takes two paths as strings to csv files that you wish to merge and
        * returns one large csv file with file 2 added to the end of file 1*/
        File outputFile = new File("mergedDataAssignment2.csv");

        //Create buffered readers and writers
        BufferedReader reader1 = null;
        BufferedReader reader2 = null;
        BufferedWriter writer = null;
        try {
            reader1 = new BufferedReader(new FileReader(path1));
            reader2 = new BufferedReader(new FileReader(path2));
            writer = new BufferedWriter(new FileWriter(outputFile));

            String currentLine;

            //Make sure file is not empty, then wrte header line (first line in csv)
            if ((currentLine = reader1.readLine()) != null) {
                writer.write(currentLine);
                writer.newLine();
            }
            //Write the rest of the lines in the file
            while ((currentLine = reader1.readLine()) != null) {
                writer.write(currentLine);
                writer.newLine();
            }

            //Skip second header and write lines into merged file
            reader2.readLine();
            while ((currentLine = reader2.readLine()) != null) {
                writer.write(currentLine);
                writer.newLine();
            }

        } catch(IOException ioEx){
                throw ioEx;
        }
        finally {
            //Close the readers and writer, handle exceptions
            try {
                if (writer != null) writer.close();
            }catch(IOException e){
                System.out.println("IOException caught while closing the writer...");
                e.printStackTrace();
            }
            try {
                if (reader1 != null) reader1.close();
            }catch(IOException e){
                System.out.println("IOException caught while closing reader1...");
                e.printStackTrace();
            }
            try {
                if (reader2 != null) reader2.close();
            }catch(IOException e){
                System.out.println("IOException caught while closing reader2...");
                e.printStackTrace();
            }        }
        return outputFile;
    }

    public static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        //Check for any commas that are between quotes (part of a data entry rather than split for a cell)
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                //Keep track of whether we are in quotes or outside of them
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {

                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    public static Map<String, Set<IndiegogoCampaign>> createCampaignMap(File csvFile) throws Exception {
        /*This function creates a map of unique campaign objects based on project_id, funds_raised_percent,
        and close_date columns */
        Map<String, Set<IndiegogoCampaign>> campaignMap = new HashMap<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(csvFile));

            String headerLine = reader.readLine();
            if (headerLine == null) return campaignMap; // empty file

            // Split header and find indices
            String[] headers = parseCsvLine(headerLine);
            Map<String, Integer> colIndex = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                colIndex.put(headers[i].trim().toLowerCase(), i);
            }

            Integer projectIdIdx = colIndex.get("project_id");
            Integer fundPercentIdx = colIndex.get("funds_raised_percent");
            Integer closeDateIdx = colIndex.get("close_date");
            Integer titleIdx = colIndex.get("title");

            //check that needed columns exist
            if (projectIdIdx == null || fundPercentIdx == null || closeDateIdx == null || titleIdx == null) {
                throw new IllegalArgumentException("CSV missing required columns");
            }

            //Get fields from line in csv and create corresponding campaign if it does not exist in current map
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);

                if (fields.length <= Math.max(titleIdx,Math.max(projectIdIdx,
                        Math.max(fundPercentIdx, closeDateIdx)))) {
                    continue;
                }

                String projectId = fields[projectIdIdx].trim();
                String fundRaisedPercent = fields[fundPercentIdx].trim();
                String closeDate = fields[closeDateIdx].trim();
                String title = fields[titleIdx].trim();

                IndiegogoCampaign campaign = new IndiegogoCampaign(projectId, fundRaisedPercent, closeDate, title);

                // Add to map, prevent duplicates automatically
                campaignMap.computeIfAbsent(title, k -> new HashSet<>()).add(campaign);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ioEx) {
                    System.out.println("IOException caught while trying to close reader in createCampaignMap()...");
                    ioEx.printStackTrace(); }
            }
        }

        return campaignMap;
    }

    public static void searchCampaignsByKeyword(Map<String, Set<IndiegogoCampaign>> campaignMap, String keyword) {
        //Search thorugh the campaign map by a specific keyword in title
        boolean found = false;

        for (Map.Entry<String, Set<IndiegogoCampaign>> entry : campaignMap.entrySet()) {
            String title = entry.getKey();

            // case-insensitive "contains"
            if (title.toLowerCase().contains(keyword.toLowerCase())) {
                found = true;

                for (IndiegogoCampaign campaign : entry.getValue()) {
                    System.out.println("Project Title: " + campaign.getTitle());
                    System.out.println("Project ID: " + campaign.getId());
                    System.out.println("Funds Raised Percent: " + campaign.getFundRaisedPercent());
                    System.out.println("Close Date: " + campaign.getCloseDate());
                    System.out.println("------------------------");
                }
            }
        }

        if (!found) {
            System.out.println("No campaigns found with keyword: " + keyword);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path1 = "C:\\Users\\kaife\\Dataforclass\\Indiegogo.csv";
        String path2 = "C:\\Users\\kaife\\Dataforclass\\Indiegogo2.csv";
        SearchHistory history = new SearchHistory();
        try {
            File masterFile = mergeFiles(path1, path2);

            // Create the map of campaigns
            Map<String, Set<IndiegogoCampaign>> campaignMap = createCampaignMap(masterFile);

            // Prompt user input
            while (true) {
                System.out.println("Would you like to 'search' keyword in campaign titles or 'print' search history? :");
                String userChoice = scanner.nextLine();
                if (userChoice.equalsIgnoreCase("search")){
                    System.out.println("Enter keyword you would like to search in campaign titles: ");
                    String userInput = scanner.nextLine();
                    searchCampaignsByKeyword(campaignMap, userInput);
                    history.addEntry(userInput);
                } else if (userChoice.equalsIgnoreCase("print")) {
                    history.printAllHistory();
                } else {
                    System.out.println("Please enter 'search' or 'print'...\n");
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception caught in main method");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception caught in main method");
        }
    }
}
