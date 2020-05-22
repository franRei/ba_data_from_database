import java.util.ArrayList;

public class DescTree {
    public ArrayList<String> words;
    public String language;
    public ArrayList<DescTree> children;
    public boolean borrowed;
    public boolean derived = false;
    public boolean error = false;

    public DescTree(String word, String language){
        words = new ArrayList<String>();
        children = new ArrayList<DescTree>();
        words.add(word);
        this.language = language;
        borrowed = false;
    }


    public DescTree(String word, String language, boolean error){
        words = new ArrayList<String>();
        children = new ArrayList<DescTree>();
        words.add(word);
        this.language = language;
        this.error=error;
    }

    public DescTree(ArrayList<String> words, String language){
        children = new ArrayList<DescTree>();
        this.words = words;
        this.language = language;
    }

    public void addChild(DescTree child){
        children.add(child);
    }

    public void setChildren() {
        this.children = children;
    }

    public void setderived (boolean derived) {
        this.derived = derived;
    }
    public boolean getderived () {
        return derived;
    }

    public ArrayList<DescTree> getChildren() {
        return children;
    }

    public void setWords() {
        this.words = words;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setLanguage() {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setBorrowed() {
        this.borrowed = true;
    }

    public boolean getBorrowed() {
        return borrowed;
    }

    public void print(){
        System.out.print(language + ": ");
        for(String word : words){
            System.out.print(word +", ");
        }
        System.out.print("(");
        for(DescTree child : children){
            child.print();
            System.out.print("; ");
        }
        System.out.print(")");
    }
}
