package com.red;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


public class CP {


    private static final int RECURSIVITY = 4;
    private static Board copy;
    private static int hor;
    private static int ver;


    private static void init(Board board){
        copy = new Board(board.getHorizontal(),board.getVertical());
        copy.setR(board.getR());
        copy.setC(board.getC());
        copy.setState(board.getState());
        hor = 0;
        ver = 0;
        Integer[][] cb = new Integer[copy.getVertical()][copy.getHorizontal()];
        for(int i=0;i<copy.getVertical();i++){
            for(int j=0;j<copy.getHorizontal();j++){
                cb[i][j] = board.getBoard()[i][j];
            }
        }
        copy.setBoard(cb);
    }

    public static int[] nextMove(Board board){
        init(board);
        max(RECURSIVITY,copy.getState());
        return new int[]{hor,ver};
    }


    private static void delete(int h, int v, int state){
        int i = state==1? 1 : 0;
        int j = state==2? 1 : 0;
        copy.getBoard()[h][v] = 0;
        copy.getBoard()[v+j][h+i] = 0;
    }

    private static void add(int h, int v, int state){
        int i = state==1? 1 : 0;
        int j = state==2? 1 : 0;
        copy.getBoard()[h][v] = state;
        copy.getBoard()[v+j][h+i] = state;
    }

    private static int max(int rec, int state) {
        if(rec==0) return available_moves(state) - available_moves(state==1?2:1);

        int evaluation = -copy.getVertical() * copy.getHorizontal();

        for (int i = copy.getR(); i < copy.getVertical(); i++) {
            for (int j = copy.getC(); j < copy.getHorizontal(); j++) {
                if (copy.check(j, i, state)) {
                    add(j, i, state);
                    int temp = min(rec--, state == 1 ? 2 : 1);
                    delete(j, i, state);
                    if (temp > evaluation) {
                        hor = j;
                        ver = i;
                        evaluation = temp;
                    }
                }
            }
        }
        return evaluation;
    }

    private static int min(int rec, int state){
        if(rec==0) return available_moves(state) - available_moves(state==1?2:1);

        int evaluation = copy.getVertical()*copy.getHorizontal();

        for(int i = copy.getR();i<copy.getVertical();i++){
            for(int j = copy.getC();j<copy.getHorizontal();j++){
                if(copy.check(j,i,state)){
                    add(j,i,state);
                    int temp = max(rec--,state==1?2:1);
                    delete(j,i,state);
                    if(temp<evaluation) {
                        evaluation = temp;
                    }
                }
            }
        }
        return evaluation;
    }



    private static int available_moves(int state){
        int res = 0;
        int h = state==1? 1 : 0;
        int v = state==2? 1 : 0;
        for(int i = copy.getR();i<copy.getVertical();i++){
            for(int j = copy.getC();j<copy.getHorizontal();j++){
                if(copy.check(j,i,state)) res++;
            }
        }
        return res;
    }
}
