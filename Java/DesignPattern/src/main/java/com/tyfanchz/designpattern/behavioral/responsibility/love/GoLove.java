package com.tyfanchz.designpattern.behavioral.responsibility.love;

import com.tyfanchz.designpattern.behavioral.responsibility.action.LoveAction;

/**
 * <p>Description:
 *
 * <p>Project: DesignPattern
 *
 * @author tyfanchz
 * @date 2020-04-07
 */
public class GoLove implements Love {
    @Override
    public LoveAction getLoveAction() {
        return LoveAction.GO;
    }

    @Override
    public void wellIWantToDo() {
        System.out.println("嘿嘿~把小甜心接回家咯！");
    }
}
