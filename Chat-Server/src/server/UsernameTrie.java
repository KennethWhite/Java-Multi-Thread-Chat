package server;

import sun.text.normalizer.Trie;

import java.util.*;

/**
 * Created by Daric on 1/31/2017.
 */
public class UsernameTrie {

    private class TrieNode{
        Map<Character, TrieNode> children = new TreeMap<>();
        boolean username = false;
    }//end private trienode

    private TrieNode root;

    public UsernameTrie(){
        this.root = new TrieNode();
    }

    public void insert(String s){
        TrieNode cur = this.root;
        for(char c: s.toCharArray()){
            TrieNode next = cur.children.get(c);
            if(next == null){
                cur.children.put(c, next = new TrieNode());
            }
            cur = next;
        }
        cur.username = true;
    }

    public boolean contains(String s){
        TrieNode cur = this.root;
        for(char c: s.toCharArray()){
            TrieNode next = cur.children.get(c);
            if(next == null){
                return false;
            }
            cur = next;
        }
        return cur.username;
    }

    public void remove(String s){
        TrieNode cur = this.root;
        for(char c: s.toCharArray()){
            TrieNode next = cur.children.get(c);
            cur = next;
        }
        cur.username = false;
    }

}//end usernametrie
