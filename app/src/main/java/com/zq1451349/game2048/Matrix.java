package com.zq1451349.game2048;

import java.util.Random;

public class Matrix {
    public static final int MOVE_LEFT = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_UP = 2;
    public static final int MOVE_DOWN = 3;

    GameActivity activity;
    Matrix[] matrixRecord = new Matrix[6];

    int stepCancelable = 5;
    int stepCanceled = 0;
    int score = 0;
    int[][] elements = new int[4][4];
    private Random random = new Random();

    public Matrix() {
    }

    public Matrix(GameActivity activity) {
        createElement();
        createElement();
        for (int i = 0; i < matrixRecord.length; i++) {
            matrixRecord[i] = new Matrix().copy(this);
        }
        this.activity = activity;
    }

    public Matrix copy(Matrix matrix) {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                this.elements[row][column] = matrix.elements[row][column];
            }
        }
        return this;
    }

    public int getMaxElement() {
        int maxElement = 2;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                maxElement = elements[row][column] > maxElement ? elements[row][column] : maxElement;
            }
        }
        return maxElement;
    }

    public void createElement() {
        int row, column;
        do {
            row = random.nextInt(4);
            column = random.nextInt(4);
        } while (elements[row][column] != 0);
        elements[row][column] = random.nextInt() % 5 == 0 ? 4 : 2;
    }

    public int move(int direction) {
        if (activity != null) {
            for (int i = matrixRecord.length - 1; i > 0; i--) {
                matrixRecord[i].copy(matrixRecord[i - 1]);
            }
            matrixRecord[0].copy(this);
        }
        boolean hasMoved = false;
        int mark = 0;
        switch (direction) {
            case MOVE_LEFT:
                for (int row = 0; row < 4; row++) {
                    for (int column = 0; column < 3; column++) {
                        if (elements[row][column] == 0)
                            continue;
                        for (int i = column + 1; i < 4; i++) {
                            if (elements[row][i] != 0 && elements[row][i] != elements[row][column])
                                break;
                            if (elements[row][i] == elements[row][column]) {
                                mark += elements[row][column] += elements[row][i];
                                elements[row][i] = 0;
                                break;
                            }
                        }
                    }
                    for (int column = 0; column < 3; column++) {
                        if (elements[row][column] != 0)
                            continue;
                        for (int i = column + 1; i < 4; i++)
                            if (elements[row][i] != 0) {
                                elements[row][column] = elements[row][i];
                                elements[row][i] = 0;
                                hasMoved = true;
                                break;
                            }
                    }
                }
                break;
            case MOVE_RIGHT:
                for (int row = 0; row < 4; row++) {
                    for (int column = 3; column > 0; column--) {
                        if (elements[row][column] == 0)
                            continue;
                        for (int i = column - 1; i >= 0; i--) {
                            if (elements[row][i] != 0 && elements[row][i] != elements[row][column])
                                break;
                            if (elements[row][i] == elements[row][column]) {
                                mark += elements[row][column] += elements[row][i];
                                elements[row][i] = 0;
                                break;
                            }
                        }
                    }
                    for (int column = 3; column > 0; column--) {
                        if (elements[row][column] != 0)
                            continue;
                        for (int i = column - 1; i >= 0; i--)
                            if (elements[row][i] != 0) {
                                elements[row][column] = elements[row][i];
                                elements[row][i] = 0;
                                hasMoved = true;
                                break;
                            }
                    }
                }
                break;
            case MOVE_UP:
                for (int column = 0; column < 4; column++) {
                    for (int row = 0; row < 3; row++) {
                        if (elements[row][column] == 0)
                            continue;
                        for (int i = row + 1; i < 4; i++) {
                            if (elements[i][column] != 0 && elements[i][column] != elements[row][column])
                                break;
                            if (elements[i][column] == elements[row][column]) {
                                mark += elements[row][column] += elements[i][column];
                                elements[i][column] = 0;
                                break;
                            }
                        }
                    }
                    for (int row = 0; row < 3; row++) {
                        if (elements[row][column] != 0)
                            continue;
                        for (int i = row + 1; i < 4; i++)
                            if (elements[i][column] != 0) {
                                elements[row][column] = elements[i][column];
                                elements[i][column] = 0;
                                hasMoved = true;
                                break;
                            }
                    }
                }
                break;
            case MOVE_DOWN:
                for (int column = 0; column < 4; column++) {
                    for (int row = 3; row > 0; row--) {
                        if (elements[row][column] == 0)
                            continue;
                        for (int i = row - 1; i >= 0; i--) {
                            if (elements[i][column] != 0 && elements[i][column] != elements[row][column])
                                break;
                            if (elements[i][column] == elements[row][column]) {
                                mark += elements[row][column] += elements[i][column];
                                elements[i][column] = 0;
                                break;
                            }
                        }

                    }
                    for (int row = 3; row > 0; row--) {
                        if (elements[row][column] != 0)
                            continue;
                        for (int i = row - 1; i >= 0; i--)
                            if (elements[i][column] != 0) {
                                elements[row][column] = elements[i][column];
                                elements[i][column] = 0;
                                hasMoved = true;
                                break;
                            }
                    }
                }
                break;
            default:
                break;
        }
        if (mark > 0) {
            hasMoved = true;
        }
        if (activity != null) {
            if (!hasMoved) {
                for (int i = 0; i < matrixRecord.length - 1; i++) {
                    matrixRecord[i].copy(matrixRecord[i + 1]);
                }
            } else {
                createElement();
                if (mark > 0) {
                    score += mark;
                    if (getMaxElement() > matrixRecord[0].getMaxElement() && getMaxElement() >= 2048)
                        stepCancelable = 5;
                }
            }
        }
        return hasMoved ? (mark > 0 ? mark : 1) : 0;
    }

    public boolean canMove() {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (elements[row][column] == 0)
                    return true;
            }
        }
        Matrix testMovable = new Matrix().copy(this);
        return testMovable.move(MOVE_LEFT) != 0 ||
                testMovable.move(MOVE_RIGHT) != 0 ||
                testMovable.move(MOVE_UP) != 0 ||
                testMovable.move(MOVE_DOWN) != 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            Matrix anotherMatrix = (Matrix) obj;
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    if (elements[row][column] != (anotherMatrix).elements[row][column])
                        return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
