package com.red;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board implements Cloneable{

    Integer[][] board;

    private Integer horizontal;
    private Integer r;
    private Integer c;
    private Integer vertical;

    private Integer state;

    private String mes;

    private Boolean cp;

    public Board(Integer horizontal, Integer vertical) {
        state = 1;
        mes = new String();
        this.horizontal = horizontal;
        this.vertical = vertical;
        c=0;
        r = 0;
        cp = false;
        board = new Integer[vertical][horizontal];
        for (int j = r; j < vertical; j++) {
            for (int k = c; k < horizontal; k++) {
                board[j][k] = 0;
            }
        }
    }


    public void horizontal(int h, int v) throws InterruptedException {
        if (check(h,v,1)) {
            board[v][h] = 1;
            board[v][h + 1] = 1;
            update();
            if(!isVer()){
                System.out.println("Horizontal Won");
                mes = "Horizontal Won";
                setState(3);
            }
            else {
                System.out.println("Vertical turn");
                mes = "Vertical turn";
                setState(2);

            }
        } else {
            System.out.println("The move isn't allowed");
            mes = "The move isn't allowed - Horizontal turn";
            setState(1);
        }
    }

    Boolean check(int h, int v, int s){
        if(s==1) return v<this.vertical && h < this.horizontal - 1 && h>=r && v>=c
                        &&board[v][h]==0
                        && board[v][h+1]==0 && state !=0;
        else return v<this.vertical -1 && h < this.horizontal && h>=r && v>=c
                    && board[v][h]==0
                    && board[v+1][h]==0 && state !=0;
    }
    public void vertical(int h, int v) throws InterruptedException {
        if (check(h,v,2)) {
            board[v][h] = 2;
            board[v + 1][h] = 2;
            update();
            if(!isHor()){
                System.out.println("Vertical won");
                mes = "Vertical won";
                setState(3);
            }
            else{
                System.out.println("Horizontal turn");
                mes = "Horizontal turn";
                setState(1);

            }
        } else {
            System.out.println("The move isn't allowed");
            mes = "The move isn't allowed - Vertical turn";
            setState(2);
        }
    }

    private void update() {
        int j = c,k =r;
        for (int i = r; i < vertical; i++){
            if (board[i][c] == 2 || board[i][c] == 1) j++;
            else if(board[i][c] == 0)
                if (i > r + 1 && board[i - 1][c] != 0)
                    if ((i < vertical - 1 && board[i + 1][c] != 0) || i == vertical-1)
                        if (c < horizontal - 1 && board[i][c + 1] != 0) j++;

            if (board[i][horizontal - 1] == 2 || board[i][horizontal - 1] == 1) k++;
            else if(board[i][horizontal-1] == 0)
                if (i > r + 1 && board[i - 1][horizontal-1] != 0)
                    if ((i < vertical - 1 && board[i + 1][horizontal-1] != 0) || i == vertical-1)
                        if (board[i][horizontal-2]!=0) k++;
        }
        if(j== vertical)
            this.r++;
        if(k== vertical) this.horizontal--;
        j = r;
        k = c;
        for (int i = c; i < horizontal; i++){
            if (board[r][i] == 2 || board[r][i] == 1) j++;
            else if(board[r][i] == 0)
                if (i > c + 1 && board[r][i-1] != 0)
                    if ((i < horizontal - 1 && board[r][i+1] != 0)||i==horizontal-1)
                        if (r < vertical - 1 && board[r+1][i] != 0) j++;

            if (board[vertical - 1][i] == 2 || board[vertical - 1][i] == 1) k++;
            else if(board[vertical-1][i] == 0)
                if (i > c + 1 && board[vertical-1][i-1] != 0)
                    if ((i < horizontal - 1 && board[vertical-1][i+1] != 0)||i==horizontal-1)
                        if (board[vertical-2][i] != 0) k++;
        }
        if(j== horizontal)
            this.c++;
        if(k== horizontal) this.vertical--;
    }



    private Boolean isHor() {
        for (Integer[] row : board)
            for (int i = 0; i < row.length - 1; i++)
                if (row[i] == 0 && row[i + 1] == 0) return true;
        return false;
    }

    private Boolean isVer(){
        for (int i = 0; i < horizontal; i++)
            for (int j = 0; j < vertical -1; j++)
                if (board[j][i] == 0 && board[j + 1][i] == 0)
                    return true;

        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
