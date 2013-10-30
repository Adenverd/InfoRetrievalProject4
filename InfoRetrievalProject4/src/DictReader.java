import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DictReader {

    public static List<DictEntry> readDict(File dict) throws IOException {
        List<DictEntry> entries = new ArrayList<DictEntry>();
        FileReader fileReader = new FileReader(dict);
        BufferedReader br = new BufferedReader(fileReader);
        String line;
        while ((line = br.readLine())!=null){
            Scanner sc = new Scanner(line);
            String term = sc.next();
            int numDocs = sc.nextInt();
            long postingLocation = sc.nextLong();
            DictEntry entry = new DictEntry(term, numDocs, null);
            entry.setPostingLocation(postingLocation);
            entries.add(entry);
        }
        return entries;
    }

    /**
     * Finds a term in a sorted list of entries using binary search
     * @param term
     * @param dictEntries
     * @return
     */
    public static DictEntry findEntry(String term, List<DictEntry> dictEntries){
        int lo = 0;
        int hi = dictEntries.size()-1;
        while (lo <= hi){
            int mid = lo + (hi - lo) /2;
            if (term.compareTo(dictEntries.get(mid).getTerm())<0){
                hi = mid -1;
            }
            else if (term.compareTo(dictEntries.get(mid).getTerm())>0){
                lo = mid + 1;
            }
            else return dictEntries.get(mid);
        }
        return null;
    }
}
