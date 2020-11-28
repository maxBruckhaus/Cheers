import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cheers {

    int MAX_PIXELS = 10000;
    HashMap<String, Integer> moteCounts = new HashMap<>();
    HashMap<String, Integer> partMoteCounts = new HashMap<>();
    private int minPixels;

    public static void main(String[] args){
        Cheers cheers = new Cheers();

        String[] motes = {"cheers", "nice"};

        // Test 1
        String messages = "cheers10 cheers5, cheers10";
        String[] expected1 = new String[]{"cheers:25"};
        String[] result = cheers.reportCheers(1, messages, motes);
        System.out.println("expected1 = " + Arrays.toString(expected1));
        System.out.println("result = " + Arrays.toString(result));
        System.out.println(Arrays.equals(expected1, result));

        // Test 2
        messages = "Wow!! Nice job. cheers10 cheers5, cheers10 nice10, nice5, cheers45 nice nice40 cool! nice10";
        String[] expected2 = new String[]{"cheers:70", "nice:65"};
        result = cheers.reportCheers(1, messages, motes);
        System.out.println("expected2 = " + Arrays.toString(expected2));
        System.out.println("result = " + Arrays.toString(result));
        System.out.println(Arrays.equals(expected2, result));
    }

    public String[] reportCheers(int minPixels, String messages, String[] motes) {
        boolean valid;
        this.minPixels = minPixels;
        initMotes(motes);
        String[] parts = messages.split(",");

        for (String part : parts) {
            partMoteCounts.clear();
            valid = getPartMoteCounts(part);
            if (valid) {
                addPartMoteCounts(partMoteCounts, moteCounts);
            }
        }

        String[] report = getReport(moteCounts);
        return report;
    }

    public String[] getReport(HashMap<String, Integer> moteCounts) {
        int size = 0;

        for (Map.Entry<String, Integer> entry : moteCounts.entrySet()){
            int count = entry.getValue();
            if (count > 0){
                size++;
            }
        }

        String[] report = new String[size];

        int index = 0;
        for (Map.Entry<String, Integer> entry : moteCounts.entrySet()) {
            String reportPart = "";
            String mote = entry.getKey();
            int count = entry.getValue();
            if (count > 0){
                reportPart = mote + ":" + count;
                report[index] = reportPart;
                index++;
            }
        }
        return report;
    }

    public void addPartMoteCounts(HashMap<String, Integer> partMoteCounts, HashMap<String, Integer> moteCounts) {
        int newCount;
        for (Map.Entry<String, Integer> entry : partMoteCounts.entrySet()) {
            String mote = entry.getKey();
            Integer count = entry.getValue();
            if (moteCounts.containsKey(mote)) {
                newCount = moteCounts.get(mote) + count;
            } else {
                newCount = count;
            }
            moteCounts.put(mote, newCount);
        }
    }

    private boolean getPartMoteCounts(String part) {
        String mote = "";
        int newCount = 0;
        String[] words = part.split(" ");

        for (String word : words) {
            int count = getCount(word);
            if (count > 0) {
                if (count < minPixels || count > MAX_PIXELS) {
                    return false;
                }
                mote = getMote(word, count);
            }
            addPartMoteCount(mote, count);
        }
        return true;
    }

    private String getMote(String word, int count) {
        String countStr = "" + count;
        String mote = word.substring(0, word.length() - countStr.length());
        return mote;
    }

    private int getCount(String word) {
        int i = word.length() - 1;
        String numStr = "";
        while (i >= 0 && word.charAt(i) >= '0' && word.charAt(i) <= '9') {
            numStr = word.charAt(i) + numStr;
            i--;
        }
        int count = 0;
        if (numStr.length() > 0) {
            count = Integer.parseInt(numStr);
        }
        return count;
    }

    private void addPartMoteCount(String mote, int count) {
        int newCount;
        if (partMoteCounts.containsKey(mote)) {
            newCount = partMoteCounts.get(mote) + count;
        } else {
            newCount = count;
        }
        partMoteCounts.put(mote, newCount);
    }

    private void initMotes(String[] motes) {
        for (String mote : motes) {
            moteCounts.put(mote, 0);
        }
    }
}
