package com.game;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Game extends JComponent {
    public final int empty = 0;
    public final int black = 10;
    public final int white = 200;
    public static int count_black = 0;
    public static int count_white = 0;
    public static int[][] field;
    public static final ArrayList<Integer> possible_moves = new ArrayList<>();
    public static boolean is_black_turn;
    public static boolean game_end;
    public static boolean computer;
    public static boolean flag = false;
    //конструктор
    public Game() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        field = new int[8][8];
        initGame();
    }
    //заполняем начальные значения
    public void initGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field[i][j] = empty;
            }
        }
        field[3][3] = white;
        field[3][4] = black;
        field[4][3] = black;
        field[4][4] = white;
        is_black_turn = true;
        game_end = false;
        ableToGo(3, 4, white);
        ableToGo(4, 3, white);
    }
    public void selectMode(boolean mode) {
        computer = mode;
    }
    //рисуем
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawGrid(graphics);
        draw(graphics);
    }
    //обработка клика мыши
    @Override
    protected void processMouseEvent (MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getButton() == MouseEvent.BUTTON1 && !game_end) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            int i = (int) ((float) y / getWidth() * 8);
            int j = (int) ((float) x / getHeight() * 8);
            if (possible_moves.size() == 0) {
                game_end = true;
                for (int b = 0; b < 8; b++) {
                    for (int l = 0; l < 8; l++) {
                        if (field[b][l] == black) {
                            count_black++;
                        }
                        if (field[b][l] == white) {
                            count_white++;
                        }
                    }
                }
                endGame();
            }
            if (computer == false) {
                for (int k = 0; k < possible_moves.size(); k += 2) {
                    if (i == possible_moves.get(k) && j == possible_moves.get(k + 1)) {
                        field[i][j] = is_black_turn ? black : white;
                        fillLine(i, j, is_black_turn ? white : black, is_black_turn ? black : white);
                        is_black_turn = !is_black_turn;
                        possible_moves.clear();
                        for (int l = 0; l < 8; l++) {
                            for (int b = 0; b < 8; b++) {
                                if (field[l][b] == (is_black_turn ? black : white)) {
                                    ableToGo(l, b, is_black_turn ? white : black);
                                }
                            }
                        }
                        break;
                    }
                }
                repaint();
            } else {
                for (int k = 0; k < possible_moves.size(); k += 2) {
                    if (i == possible_moves.get(k) && j == possible_moves.get(k + 1)) {
                        flag = true;
                        field[i][j] = black;
                        fillLine(i, j, white, black);
                        is_black_turn = !is_black_turn;
                        possible_moves.clear();
                        for (int l = 0; l < 8; l++) {
                            for (int b = 0; b < 8; b++) {
                                if (field[l][b] == white) {
                                    ableToGo(l, b, black);
                                }
                            }
                        }
                        repaint();
                    }
                }
                if (flag == true) {
                    i = possible_moves.get(0);
                    j = possible_moves.get(1);
                    System.out.println(possible_moves);
                    field[i][j] = white;
                    fillLine(i, j, black, white);
                    possible_moves.clear();
                    for (int l = 0; l < 8; l++) {
                        for (int b = 0; b < 8; b++) {
                            if (field[l][b] == black) {
                                ableToGo(l, b, white);
                            }
                        }
                    }
                    flag = false;
                    repaint();
                }
            }
        }
    }
    //метод отрисовки поля 8х8
    void drawGrid(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        int d_height = height / 8;
        int d_width = width / 8;
        graphics.setColor(Color.black);
        for (int i = 0; i < 8; i++) {
            graphics.drawLine(0, d_height * i, width, d_height * i);
            graphics.drawLine(d_width * i, 0, d_width * i, height);
        }
    }
    //метод отрисовки черной фигуры
    public void drawBlack(int i, int j, Graphics graphics) {
        graphics.setColor(Color.black);
        int d_height = getHeight() / 8;
        int d_width = getWidth() / 8;
        int x = j * d_width;
        int y = i * d_height;
        graphics.fillOval(x + 5 * d_width / 100, y, d_width * 9 / 10, d_height);
    }
    //метод отрисовки белой фигуры
    public void drawWhite(int i, int j, Graphics graphics) {
        graphics.setColor(Color.lightGray);
        int d_height = getHeight() / 8;
        int d_width = getWidth() / 8;
        int x = j * d_width;
        int y = i * d_height;
        graphics.fillOval(x + 5 * d_width / 100, y, d_width * 9 / 10, d_height);
    }
    //рисуем изменения на поле
    public void draw(Graphics graphics) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] == black) {
                    drawBlack(i, j, graphics);
                }
                else if (field[i][j] == white) {
                    drawWhite(i, j, graphics);
                }
            }
        }
        for (int i = 0; i < possible_moves.size(); i += 2) {
            drawPossible(possible_moves.get(i + 1), possible_moves.get(i), graphics);
        }
    }
    //доступные ходы для черных
    public void ableToGo(int i, int j, int color) {
        int temp_i = i;
        int temp_j = j;
        //можем ли пойти вниз
        while (temp_i < 7 && field[temp_i + 1][j] == color) {
            temp_i++;
            if (temp_i + 1 != 8 && field[temp_i + 1][j] == empty) {
                possible_moves.add(temp_i + 1);
                possible_moves.add(j);
                break;
            }
        }
        temp_i = i;
        //можем ли пойти вверх
        while (temp_i > 0 && field[temp_i - 1][j] == color) {
            temp_i--;
            if (temp_i - 1 != -1 && field[temp_i - 1][j] == empty) {
                possible_moves.add(temp_i - 1);
                possible_moves.add(j);
                break;
            }
        }
        temp_j = j;
         //можем ли пойти направо
        while (temp_j < 7 && field[i][temp_j + 1] == color) {
            temp_j++;
            if (temp_j + 1 != 8 && field[i][temp_j + 1] == empty) {
                possible_moves.add(i);
                possible_moves.add(temp_j + 1);
                break;
            }
        }
        temp_j = j;
        //можем ли пойти налево
        while (temp_j > 0 && field[i][temp_j - 1] == color) {
            temp_j--;
            if (temp_j - 1 != -1 && field[i][temp_j - 1] == empty) {
                possible_moves.add(i);
                possible_moves.add(temp_j - 1);
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //можем ли пойти по диагонали вниз вправо
        while (temp_j > 0 && temp_i < 7 && field[temp_i + 1][temp_j - 1] == color) {
            temp_j--;
            temp_i++;
            if (temp_j - 1 != -1 && temp_i + 1 != 8 && field[temp_i + 1][temp_j - 1] == empty) {
                possible_moves.add(temp_i + 1);
                possible_moves.add(temp_j - 1);
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //можем ли пойти по диагонали вниз налево
        while (temp_j < 7 && temp_i < 7 && field[temp_i + 1][temp_j + 1] == color) {
            temp_j++;
            temp_i++;
            if (temp_j + 1 != 8 && temp_i + 1 != 8 && field[temp_i + 1][temp_j + 1] == empty) {
                possible_moves.add(temp_i + 1);
                possible_moves.add(temp_j + 1);
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //можем ли пойти по диагонали вверх направо
        while (temp_j < 7 && temp_i > 0 && field[temp_i - 1][temp_j + 1] == color) {
            temp_j++;
            temp_i--;
            if (temp_j + 1 != 8 && temp_i - 1 != -1 && field[temp_i - 1][temp_j + 1] == empty) {
                possible_moves.add(temp_i - 1);
                possible_moves.add(temp_j + 1);
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //можем ли пойти по диагонали вверх налево
        while (temp_j > 0 && temp_i > 0 && field[temp_i - 1][temp_j - 1] == color) {
            temp_j--;
            temp_i--;
            if (temp_j - 1 != -1 && temp_i - 1 != -1 && field[temp_i - 1][temp_j - 1] == empty) {
                possible_moves.add(temp_i - 1);
                possible_moves.add(temp_j - 1);
                break;
            }
        }
    }

    public void drawPossible(int i, int j, Graphics graphics) {
        graphics.setColor(Color.black);
        int d_height = getHeight() / 8;
        int d_width = getWidth() / 8;
        int x = i * d_width;
        int y = j * d_height;
        graphics.drawOval(x + 5 * d_width / 100, y, d_width * 9 / 10, d_height);
    }
    public void fillLine(int i, int j, int opposite_color, int current_color) {
        int temp_i = i;
        int temp_j = j;
        //заполняем снизу вверх
        while (temp_i < 7 && field[temp_i + 1][j] == opposite_color) {
            temp_i++;
            if (temp_i + 1 != 8 && field[temp_i + 1][j] == current_color) {
                for (int b = i; b < temp_i + 1; b++) {
                    field[b][j] = current_color;
                }
                break;
            }
        }
        temp_i = i;
        //заполняем сверху вниз
        while (temp_i > 0 && field[temp_i - 1][j] == opposite_color) {
            temp_i--;
            if (temp_i - 1 != -1 && field[temp_i - 1][j] == current_color) {
                for (int b = i; b > temp_i - 1; b--) {
                    field[b][j] = current_color;
                }
                break;
            }
        }
        temp_j = j;
        //заполняем слева направо
        while (temp_j > 0 && field[i][temp_j - 1] == opposite_color) {
            temp_j--;
            if (temp_j - 1 != -1 && field[i][temp_j - 1] == current_color) {
                for (int b = j; b > temp_j - 1; b--) {
                    field[i][b] = current_color;
                }
                break;
            }
        }
        temp_j = j;
        //заполняем справа налево
        while (temp_j < 7 && field[i][temp_j + 1] == opposite_color) {
            temp_j++;
            if (temp_j + 1 != 8 && field[i][temp_j + 1] == current_color) {
                for (int b = j; b < temp_j + 1; b++) {
                    field[i][b] = current_color;
                }
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //заполняем нижнюю левую диагональ
        while (temp_j < 7 && temp_i > 0 && field[temp_i - 1][temp_j + 1] == opposite_color) {
            temp_j++;
            temp_i--;
            if (temp_j + 1 != 8 && temp_i - 1 != -1 && field[temp_i - 1][temp_j + 1] == current_color) {
                for (int b = j, k = i; b < temp_j + 1 && k > temp_i - 1; b++, k--) {
                    field[k][b] = current_color;
                }
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //заполняем нижнюю левую диагональ
        while (temp_j > 0 && temp_i > 0 && field[temp_i - 1][temp_j - 1] == opposite_color) {
            temp_j--;
            temp_i--;
            if (temp_j - 1 != -1 && temp_i - 1 != -1 && field[temp_i - 1][temp_j - 1] == current_color) {
                for (int b = j, k = i; b > temp_j - 1 && k > temp_i - 1; b--, k--) {
                    field[k][b] = current_color;
                }
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //заполняем верхнюю левую диагональ
        while (temp_j < 7 && temp_i < 7 && field[temp_i + 1][temp_j + 1] == opposite_color) {
            temp_j++;
            temp_i++;
            if (temp_j + 1 != 8 && temp_i + 1 != 8 && field[temp_i + 1][temp_j + 1] == current_color) {
                for (int b = j, k = i; b < temp_j + 1 && k < temp_i + 1; b++, k++) {
                    field[k][b] = current_color;
                }
                break;
            }
        }
        temp_i = i;
        temp_j = j;
        //заполняем верхнюю левую диагональ
        while (temp_j > 0 && temp_i < 7 && field[temp_i + 1][temp_j - 1] == opposite_color) {
            temp_j--;
            temp_i++;
            if (temp_j - 1 != -1 && temp_i + 1 != 8 && field[temp_i + 1][temp_j - 1] == current_color) {
                for (int b = j, k = i; b > temp_j - 1 && k < temp_i + 1; b--, k++) {
                    field[k][b] = current_color;
                }
                break;
            }
        }
    }
    public static void endGame() {
        String result;
        if (count_black > count_white) {
            result = "Черные победили";
        } else {
            result = "Белые победили";
        }
        System.out.println(result + " со счетом " + count_black + " : " + count_white);
    }
}
