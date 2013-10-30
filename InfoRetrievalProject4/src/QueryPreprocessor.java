public class QueryPreprocessor {
    public static String preprocess(String term){
        String token = term;
        String stopChars = "\t\n\r\f\"\1\u000B,*#$^&+=!?-_()[]{}|\\:<>;/%'";
        for (Character c : stopChars.toCharArray()){
            token = token.replace(c.toString(),"");
        }
        return token.toLowerCase();
    }
}
