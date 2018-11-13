package test;

import android.content.Context;
import android.widget.Toast;

import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.List;

/**
 * Created by ThangBK on 22/3/2017.
 */

public class TestUltis {
    public static List<WordInfo> sListLearnedWord;
    public static List<WordInfo> sListAllWord;
    //public static List<PharseInfo> sListLearnedPharse;

    /**
     * Tạo ra ba câu hỏi ngẫu nhiên
     * @param testWord
     * @return
     */
    public static String getRandomAnswer(WordInfo testWord) {
        String result = "";
        int count = 0;
        int random = AppUltis.randomInt(0, TestUltis.sListAllWord.size() - 1);
        while (!TestUltis.sListAllWord.get(random).getName().equals(testWord.getName())
                && !result.contains(TestUltis.sListAllWord.get(random).getMeaning())
                && count < 3) {
            result = result + TestUltis.sListAllWord.get(random).getMeaning() + "::";
            count++;
            random = AppUltis.randomInt(0, TestUltis.sListAllWord.size() - 1);
        }
        return result;
    }

    public static void showDialogComplete(Context context) {
        Toast.makeText(context, "Complete",Toast.LENGTH_SHORT).show();
    }
}
