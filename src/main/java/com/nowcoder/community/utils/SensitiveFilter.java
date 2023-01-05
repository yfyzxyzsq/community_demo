package com.nowcoder.community.utils;

import com.nowcoder.community.constant.SensitiveReplacement;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:敏感词过滤器
 * @Author:DDD_coder
 * @Date:2023/1/5 16:30
 */

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private TrieNode root = new TrieNode();


    @PostConstruct
    public void init(){
        try(
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ) {

            String keyword;
            while ((keyword = bufferedReader.readLine()) != null){
                this.addKeyword(keyword);
            }

        } catch (Exception e) {
            logger.error("初始化前缀树失败：" + e.getMessage());
        }
    }

    public String filter(String keyword){
        TrieNode tempNode = root;

        int start = 0;
        int end = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while(end < keyword.length()){
            char c = keyword.charAt(end);

            if(isSymbol(c)){
                if(tempNode == root){
                    start++;
                    stringBuilder.append(c);
                }
                end++;
                continue;
            }
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                stringBuilder.append(keyword.charAt(start));
                tempNode = root;
                start++;
                end = start;
            }else if(subNode.isKeywordEnd()){
                stringBuilder.append(SensitiveReplacement.REPLACEMENT);
                tempNode = root;
                end++;
                start = end;
            }else{
                tempNode = subNode;
                end++;
            }
        }
        stringBuilder.append(keyword.substring(start,end));

        return stringBuilder.toString();
    }


    /**
     *com.nowcoder.community.utils.SensitiveFilter.isSymbol();
     *@Description:判断传入的字符是否符号，如果是符号则要分情况跳过
     *@ParamType:[char]
     *@ParamName:[c]
     *@Return:boolean
     *@author fyd
     *@create 2023/1/5 17:37
     *@version:
     */
    private boolean isSymbol(char c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private void addKeyword(String keyword){
        TrieNode trieNode = root;
        for(int i = 0;i < keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode temp = trieNode.getSubNode(c);
            if(temp == null){
                temp = new TrieNode();
                trieNode.addSubNodes(c, temp);
            }
            trieNode = temp;

            if(i == keyword.length() - 1){
                trieNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * @Description:前缀树的定义
     * @Author:DDD_coder
     * @Date:2023/1/5 16:30
     */
    private class TrieNode{
        private boolean isKeywordEnd = false;

        Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNodes(Character c, TrieNode trie){
            subNodes.put(c, trie);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
