import java.time.Instant;

public class SearchEntry {
    private String keyword;
    private Instant time;

    public SearchEntry(String keyword){
        this.time = Instant.now();
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }

    public Instant getTime() {
        return time;
    }

}
