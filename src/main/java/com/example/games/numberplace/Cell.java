package com.example.games.numberplace;

/**
 * このクラスは、ナンバープレースの空白セルの座標と
 * 次に計算する対象のセルクラスを保持します。
 */
class Cell {
    Cell(int y, int x) {
        this.y = y;
        this.x = x;
    }

    private Cell nextCell;
    private int y;
    private int x;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    /**
     * 次の計算対象設定
     *
     * @param nextCell 次の計算対象を設定します。
     */
    void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    /**
     * 次の計算対象
     *
     * @return 次の計算対象。
     */
    Cell next() {
        return this.nextCell;
    }
}