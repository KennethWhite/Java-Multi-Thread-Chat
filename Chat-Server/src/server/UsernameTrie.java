package server;

import sun.text.normalizer.Trie;

import java.util.*;

/**
 * A Trie structure that is used to contain usernames in the program. Used for easy quick check, insertion, removal
 * of usernames.
 *
 * Created by Daric on 1/31/2017.
 */
public class UsernameTrie {

    /**
     * Private Nested TrieNode class.
     *
     */
    private class TrieNode{
        Map<Character, TrieNode> children = new TreeMap<>();
        boolean username = false;
    }//end private trienode

    private TrieNode root;

    /**
     * Default Constructor of the Trie.
     *
     */
    public UsernameTrie(){
        this.root = new TrieNode();
    }

    /**
     * Inserts the given String s as a username in the Trie.
     *
     * @param s
     */
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

    /**
     * Checks the Trie to determine if the given String s is a username in use.
     *
     * @param s
     * @return
     */
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

    /**
     * Removes the given String s as a username from the Trie if it exists.
     *
     * @param s
     */
    public void remove(String s){
        TrieNode cur = this.root;
        for(char c: s.toCharArray()){
            TrieNode next = cur.children.get(c);
            cur = next;
        }
        cur.username = false;
    }

}//end usernametrie
