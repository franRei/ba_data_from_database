

public class Searcher {
    public String search_description(String description){
        int i = 0;
        String[] lines = description.split("line.separator");
        for(String line : lines){
            if(line.contains("{{desctree")) {
                String subString = line.substring(line.indexOf("{{desctree"),line.indexOf("}}",line.indexOf("{{desctree"))+2);
                System.out.println(subString);
                String newSub = subString.substring(subString.lastIndexOf("|")+1,subString.length()-2);
                if(newSub.matches(".*\\d.*")){
                    newSub = newSub.substring(newSub.lastIndexOf("|")+1);
                }
                System.out.println("WORT: " + newSub);
                return newSub;
            }
        }
        return "";
    }

    public String search_for_Links (String entry, String description_search, String descTree) {

        String[] lines = description_search.split("line.separator");
        if(descTree.equals(entry)){
            return entry;
        }
            for(String line : lines) {
                if(line.contains("{{also|")) {
                    String[] word = line.split("|");
                    for(String words : word){
                        if(words.equals(entry)){
                            return entry;
                        }
                    }
                }
            }
        return "false";
    }
}
