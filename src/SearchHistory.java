import java.util.List;
import java.util.ArrayList;
public class SearchHistory {
    private final List<SearchEntry> entries = new ArrayList<>();

    public void addEntry(String keyword){
        entries.add(new SearchEntry(keyword));
    }

    public void printAllHistory(){
        if (entries.isEmpty()){
            System.out.println("No recent search history...");
            return;
        }
        printTotalSearchCount();
        for (SearchEntry entry: entries){
            printEntry(entry);
        }
    }
    public void printTotalSearchCount() {
        System.out.println("-----------\nTotal searches: " + entries.size() +"\n----------");
    }
    private int getFrequencyForKeyword(String keyword) {
        int count = 0;

        for (SearchEntry entry : entries) {
            if (entry.getKeyword().equalsIgnoreCase(keyword)) {
                count++;
            }
        }
        return count;
    }


    public void printTimestamps() {
        if (entries.isEmpty()) {
            System.out.println("No search history.");
            return;
        }

        for (SearchEntry entry : entries) {
            System.out.println(entry.getKeyword() + " @ " + entry.getTime());
        }
    }




    private void printEntry(SearchEntry entry) {
        int freq = getFrequencyForKeyword(entry.getKeyword());
        System.out.println("Keyword: " + entry.getKeyword());
        System.out.println("Time: " + entry.getTime());
        System.out.println("Search frequency: " + freq);
        System.out.println("---------------------");
    }
}

