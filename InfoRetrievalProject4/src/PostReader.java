import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostReader {

    public static List<Posting> getPostings(File postingsFile, long start, int numDocs) throws IOException {
        List<Posting> postings = new ArrayList<Posting>();
        RandomAccessFile randomAccessFile = new RandomAccessFile(postingsFile, "r");
        randomAccessFile.seek(start); //2 bytes per char
        for (int i = 0; i < numDocs; i++){
            String line = randomAccessFile.readLine();
            postings.add(parsePosting(line));
        }
        return postings;
    }

    private static Posting parsePosting(String line){
        Scanner sc = new Scanner(line);
        int docId = sc.nextInt();
        float weight = sc.nextFloat();
        return new Posting(docId, weight);
    }
}
