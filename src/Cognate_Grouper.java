import java.util.ArrayList;
import java.util.HashMap;

public class Cognate_Grouper {
    int counter=0;
    HashMap<Integer, ArrayList<String>> cognates;
    ArrayList<DescTree> res;
    int id;

    public Cognate_Grouper (ArrayList<DescTree> results) {
        int counter = 0;
        cognates = new HashMap<>();
        res = results;
        id = 0;
    }


    public void run() {
        for (DescTree tree : res) {
            ArrayList<String> words = tree.getWords();
            ArrayList<DescTree> word_tree = tree.getChildren();
            counter++;

            addTree(word_tree, tree.getBorrowed());
        }

    }

    public HashMap<Integer, ArrayList<String>> getCognates(){
        return cognates;
    }



    public void addTree(ArrayList<DescTree> w, boolean bor) {
        ArrayList<String> lang_list = new ArrayList<>();
        lang_list.add("de");
        lang_list.add("en");
        lang_list.add("nl");
        lang_list.add("sv");
        lang_list.add("sw");
        lang_list.add("da");
        lang_list.add("no");
        lang_list.add("is");
        lang_list.add("nn");
        lang_list.add("nb");





        for(DescTree child : w){
            ArrayList<String> result_list = new ArrayList<>();
            ArrayList<DescTree> child2 = child.getChildren();
            ArrayList<String> words2 = child.getWords();
            String langs = child.getLanguage();
            boolean isBorrowed = bor || child.getBorrowed();
            boolean wordIsHere = false;

            String borrow = "word is borrowed from somewhere";

            for(String word2 : words2) {
                /*for(String language : lang_list){
                    if(language.equals(langs)) {
                        wordIsHere = true;
                    }
                }*/

                if(lang_list.contains(langs)){
                    id++;
                    result_list.add(word2);
                    result_list.add(langs);
                    result_list.add(Integer.toString(counter));
                    result_list.add(Boolean.toString(isBorrowed));

                    if(isBorrowed){
                        System.out.println("Language: " + langs + ", word: " + word2 + ", counter: " + counter + ", is borrowed: " + isBorrowed);
                        System.out.println(borrow);
                    }else {
                        System.out.println("Language: " + langs + ", word: " + word2 + ", counter: " + counter + ", is borrowed: " + isBorrowed);
                    }
                    cognates.put(id, result_list);
                }

            }
            addTree(child2, isBorrowed);
        }
    }
}
