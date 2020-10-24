package tk.deriwotua.service.impl;

import tk.deriwotua.service.HelloService;

/**
 * hello
 */
public class HelloServiceImpl implements HelloService {
    private String words;

    private String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String sayHello() {
        return "hello, " + words;
    }
}
