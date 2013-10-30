import java.io.File;
import java.io.IOException;
import java.util.*;

public class Retrieve {
    public static void main (String[] args) throws IOException {
        //Parse args
        File indexDir = null;
        File dict = null;
        File post = null;
        List<String> terms = new ArrayList<String>();
        Map<String, Double> weights = new HashMap<String, Double>();
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-q")){
                i++;
                while(!args[i].startsWith("-")&& i < args.length){
                    String token = QueryPreprocessor.preprocess(args[i]);
                    terms.add(token);
                    i++;
                }
                i--;
            }
            else if (args[i].equals("-w")){
                i++;
                int weightsCounter = 0;
                while(!args[i].startsWith("-") && i < args.length){
                    weights.put(terms.get(weightsCounter), Double.parseDouble(args[i]));
                    i++;
                    weightsCounter++;
                }
                i--;
            }
            else if (args[i].equals("-d")){
                i++;
                indexDir = new File(args[i]);
                dict = new File(indexDir.getAbsolutePath().concat("\\dict.txt"));
                post = new File(indexDir.getAbsolutePath().concat("\\post.txt"));
            }
        }

        //read dict into memory
        List<DictEntry> dictEntries = DictReader.readDict(dict);
        //create array accumulator
        Ranker<Integer> accumulator = new Ranker<Integer>(); //maps docId to weight. HashMap-based.
        //process each query term
        for (String term : terms){
            DictEntry entry = DictReader.findEntry(term, dictEntries);
            if (entry!=null){ //if term is in dict
                //go to postings location (random access file)
                //read numDocs number of docId/weight pairs, adding to the accumulator
                List<Posting> postings = PostReader.getPostings(post, entry.getPostingLocation(), entry.getNumDocs());
                for (Posting posting : postings){
                    if(weights.containsKey(term)){
                        accumulator.increase(posting.getDocId(), weights.get(term)*posting.getWeight());
                    }
                    else {
                        accumulator.increase(posting.getDocId(), (double)posting.getWeight());
                    }
                }
            }
        }

        int numResults = accumulator.getSize()<10 ? accumulator.getSize() : 10;
        for (int i =0; i < numResults; i++){ //get the top 10 documents from the accumulator (no need to sort the whole thing)
            int docId = accumulator.getMax();
            double weight = accumulator.get(docId);
            accumulator.remove(docId);
            System.out.println(docId + "\t" + weight);
        }

    }


}
