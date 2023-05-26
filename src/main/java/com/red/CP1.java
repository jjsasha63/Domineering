package com.red;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CP1 {


    private static final int TRIES = 10; //number of tries it performs to find the best move
    private static Board copy;
    private static int op,cur;


    private static void init(Board board){
        copy = new Board(board.getHorizontal(),board.getVertical());
        copy.setR(board.getR());
        copy.setC(board.getC());
        copy.setState(board.getState());
        Integer[][] cb = new Integer[copy.getVertical()][copy.getHorizontal()];
        for(int i=0;i<copy.getVertical();i++){
            for(int j=0;j<copy.getHorizontal();j++){
                cb[i][j] = board.getBoard()[i][j];
            }
        }
        copy.setBoard(cb);
        op = available_moves(copy.getState()==1?2:1);
        cur = available_moves(copy.getState());
    }

    public static int[] nextMove(Board board){
        init(board);
        return find(copy.getState());
    }


    private static void delete(int h, int v, int state){
        int i = state==1? 1 : 0;
        int j = state==2? 1 : 0;
        copy.getBoard()[v][h] = 0;
        copy.getBoard()[v+j][h+i] = 0;
    }

    private static void add(int h, int v, int state){
        int i = state==1? 1 : 0;
        int j = state==2? 1 : 0;
        copy.getBoard()[v][h] = state;
        copy.getBoard()[v+j][h+i] = state;
    }


    private static int[] find(int state){
        int sc = Integer.MIN_VALUE;
        int h = 0;
        int v = 0;
        Random random = new Random();
        List<Integer[]> moves = steps(state);
        for(int i=TRIES>=moves.size()?TRIES:moves.size();i>0;i--){
            int tmp;
            if(moves.size()>0) tmp = random.nextInt(moves.size());
            else tmp = 0;
            if(copy.check(moves.get(tmp)[0],moves.get(tmp)[1],state)){
                add(moves.get(tmp)[0],moves.get(tmp)[1],state);
                int temp = available_moves(state==1?2:1);
                int temp_c = available_moves(state);
                delete(moves.get(tmp)[0],moves.get(tmp)[1],state);
                if(op>temp|| cur-1<=temp_c || (h==0&&v==0) || temp==0){
                    op  = temp;
                    cur = temp_c;
                    h = moves.get(tmp)[0];
                    v = moves.get(tmp)[1];
                }
            } else i++;
        }

        return new int[]{h,v};
    }


    private static int available_moves(int state){
        int res = 0;
        int v = state==2?1:0;
        int h = state==1?1:0;
        for(int i = copy.getR();i<copy.getVertical()-v;i++){
            for(int j = copy.getC();j<copy.getHorizontal()-h;j++){
                if(copy.check(j,i,state)) res++;
            }
        }
        return res;
    }

    private static List<Integer[]> steps(int state){
        List<Integer[]> moves = new ArrayList<>();
        int v = state==2?1:0;
        int h = state==1?1:0;
        for(int i = copy.getR();i<copy.getVertical()-v;i++){
            for(int j = copy.getC();j<copy.getHorizontal()-h;j++){
                if(copy.check(j,i,state)) moves.add(new Integer[]{j,i});
            }
        }
        return moves;
    }


    }
