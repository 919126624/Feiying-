package com.wuyizhiye.framework.compoment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName PinyinController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="framework/pinyin/*")
public class PinyinController extends BaseController {

	@RequestMapping(value="getPinyin")
	public void getPinyin(@RequestParam(value="value")String value,HttpServletResponse response) throws BadHanyuPinyinOutputFormatCombination{
		Map<String,String> result = new HashMap<String, String>();
		if(!StringUtils.isEmpty(value)){
			result.put("simple", PinyinController.getSpell(value, true));
			result.put("full", PinyinController.getSpell(value, false));
		}
		outPrint(response, JSONObject.fromObject(result));
	}
	
	public static String getSpell(String target, boolean isSimpleSpell) {
        char[] chars = target.toCharArray();
        StringBuffer spell = new StringBuffer();
        String[] pinyinStr;
        int endIndex;
        for (char ch : chars) {
            // 如果不是汉字
            if (ch <= 128) {
                spell.append(ch);
                continue;
            }
            pinyinStr = PinyinHelper.toHanyuPinyinStringArray(ch);
            // 过滤中文符号
            if (pinyinStr == null) {
                spell.append(ch);
                continue;
            }
            endIndex = isSimpleSpell ? 1 : pinyinStr[0].length() - 1;
            spell.append(pinyinStr[0].substring(0, endIndex));
        }
        return spell.toString();
    }
    
    public static String[] getSpellToArray(String target){
        char[] chars = target.toCharArray();
        String[] spell = new String[chars.length];
        String[] pinyinStr;
        for (int i = 0; i < chars.length; i++) {
            // 如果不是汉字
            if (chars[i] <= 128) {
                spell[i] = String.valueOf(chars[i]);
                continue;
            }
            pinyinStr = PinyinHelper.toHanyuPinyinStringArray(chars[i]);
            // 过滤中文符号
            if (pinyinStr == null) {
                spell[i] = String.valueOf(chars[i]);
                continue;
            }
            spell[i] = pinyinStr[0].substring(0, pinyinStr[0].length() - 1);
        }
        return spell;
    }
}
