package com.example.games.numberplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * ナンバープレースの計算を行うクラスです。
 */
public class Solver {
    // ロガー
    private static final Logger logger = LogManager.getLogger();

    // ナンバープレースのデータ
    private static int[][] puzzle = new int[9][9];


    /**
     * 実行
     *
     * @param args パラメータ(特に使いません)
     */
    public static void main(String[] args) {
        csvLoader();
        outResultLog();
        // 計算対象の取得
        ArrayList<Cell> targets = getCalcTargets();

        // 計算対象の一つ目を渡す
        calc(targets.get(0));
        outResultLog();
    }

    /**
     * 計算対象取得<br>
     * {@link Cell}にセットします。
     *
     * @return 計算対象のアレイリスト
     */
    private static ArrayList<Cell> getCalcTargets() {
        ArrayList<Cell> targets = new ArrayList<>();
        int count = 0;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (puzzle[y][x] == 0) {
                    Cell cell = new Cell(y, x);
                    targets.add(cell);
                    if (count > 0) {
                        targets.get(count - 1).setNextCell(cell);
                    }
                    count++;
                }
            }
        }
        targets.get(count - 1).setNextCell(null);

        return targets;
    }

    /**
     * 計算（再帰）<br>
     * 該当セルに使用可能な値を再帰的に計算していきます。
     *
     * @param target 計算対象{@link Class}のアレイリスト
     * @return 計算可否。成功していれば最終的にtrueを返して終了します。
     */
    private static boolean calc(Cell target) {
        // 最後まで到達出来ればOK
        if (target == null) {
            // 次の計算対象がない為、算出成功
            return true;
        }

        for (int value = 1; value <= 9; value++) {
            // 不要な計算をしない為、対象を制限
            if (isAvailable(target.getY(), target.getX(), value)) {

                // 条件を満たした値をセルにセットする。
                puzzle[target.getY()][target.getX()] = value;
                if (calc(target.next())) {
                    return true;
                }
            }
        }

        // 対象がなかったので0にする。
        puzzle[target.getY()][target.getX()] = 0;
        return false;
    }

    /**
     * 使用可能判定<br>
     * 渡されたパラメータを元に、その座標で、数値が使用可能かどうか判断します。
     *
     * @param posY  計算対象のY軸座標
     * @param posX  計算対象のX軸座標
     * @param value 使用したい値
     * @return 使用可能:true / 使用不可:false
     */
    private static boolean isAvailable(int posY, int posX, int value) {
        int groupY = posY / 3;
        int groupX = posX / 3;
        for (int i = 0; i < 9; i++) {
            if (puzzle[posY][i] == value) {
                return false;
            }
            if (puzzle[i][posX] == value) {
                return false;
            }
            if (puzzle[groupY * 3 + i / 3][groupX * 3 + i % 3] == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 結果出力<br>
     * 計算結果を区切って出力します。
     */
    private static void outResultLog() {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                result.append(puzzle[y][x]).append("|");
            }
            result.append("\r\n");
        }
        logger.debug("\r\n" + result);
    }

    /**
     * CSV読込み<br>
     * 本クラスと同じパッケージ階層のリソースフォルダに置いた「numberplace.csv」を読込みます。<br>
     * 文字コードは、SJISを想定しています。
     */
    private static void csvLoader() {
        URL url = Solver.class.getResource("numberplace.csv");
        String regex = ",";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("Windows-31j")))) {
            String line;
            int y = 0, x = 0;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(regex, -1);
                for (String col : cols) {
                    if (col.equals("")) {
                        puzzle[y][x] = 0;
                    } else {
                        puzzle[y][x] = Integer.parseInt(col);
                    }
                    x++;
                }
                x = 0;
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
